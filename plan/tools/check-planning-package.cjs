// Documentation checks only. Run from any directory with the existing Node.js runtime.
// This does not validate product behavior, approve decisions, or open delivery gates.
const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const root = path.resolve(__dirname, '../..');
const read = file => fs.readFileSync(file, 'utf8');
const relative = file => path.relative(root, file).replace(/\\/g, '/');
const errors = [];
const check = (condition, message) => { if (!condition) errors.push(message); };
const field = (body, name) => (body.match(new RegExp(`^${name}: (.*)$`, 'm')) || [])[1]?.trim() || '';
const ids = text => [...text.matchAll(/\bM\d+\.\d+\b/g)].map(match => match[0]);
const same = (a, b) => JSON.stringify([...a].sort()) === JSON.stringify([...b].sort());
function markdownFiles(dir) {
  return fs.readdirSync(dir, { withFileTypes: true }).flatMap(entry => {
    const file = path.join(dir, entry.name);
    return entry.isDirectory() ? markdownFiles(file) : entry.name.endsWith('.md') ? [file] : [];
  });
}
const documents = [
  ...fs.readdirSync(root).filter(name => name.endsWith('.md')).map(name => path.join(root, name)),
  ...markdownFiles(path.join(root, 'plan')),
  ...markdownFiles(path.join(root, 'docs')),
];
const prose = body => body.replace(/^```[^\n]*\n[\s\S]*?^```\s*$/gm, '');
function anchors(body) {
  const seen = new Map();
  return new Set([...prose(body).matchAll(/^#{1,6}\s+(.+?)\s*#*\s*$/gm)].map(match => {
    const slug = match[1].toLowerCase().replace(/[^\p{L}\p{N}_\-\s]/gu, '').replace(/\s/g, '-');
    const count = seen.get(slug) || 0;
    seen.set(slug, count + 1);
    return slug + (count ? `-${count}` : '');
  }));
}
let localLinks = 0;
let anchorLinks = 0;
for (const file of documents) {
  // The planning package uses inline Markdown links, with optional angle brackets.
  for (const match of prose(read(file)).matchAll(/!?\[[^\]\n]*\]\((<[^>]+>|[^\s)]+)\)/g)) {
    const href = match[1].replace(/^<|>$/g, '');
    if (/^[a-z][a-z\d+.-]*:/i.test(href) || href.startsWith('//')) continue;
    localLinks++;
    const [target, fragment] = href.split('#');
    const resolved = target ? path.resolve(path.dirname(file), decodeURIComponent(target)) : file;
    if (!fs.existsSync(resolved)) { errors.push(`${relative(file)}: missing link ${href}`); continue; }
    if (fragment && resolved.endsWith('.md')) {
      anchorLinks++;
      check(anchors(read(resolved)).has(decodeURIComponent(fragment)), `${relative(file)}: missing anchor ${href}`);
    }
  }
}
const master = read(path.join(root, 'plan/MASTER_PLAN.md'));
const index = read(path.join(root, 'plan/subplans/README.md'));
const taskFiles = fs.readdirSync(path.join(root, 'plan/subplans')).filter(name => /^m\d+-\d+-.+\.md$/.test(name));
const tasks = new Map();
for (const filename of taskFiles) {
  const body = read(path.join(root, 'plan/subplans', filename));
  const id = (body.match(/^# (M\d+\.\d+): /) || [])[1];
  check(id && !tasks.has(id), `${filename}: missing or duplicate task ID`);
  if (!id) continue;
  tasks.set(id, { filename, body, dependencies: ids(field(body, 'Blocked by')) });
  for (const label of ['Task status', 'Parent', 'Requirements', 'Blocked by', 'Open decisions', 'Public boundaries']) {
    check(field(body, label), `${id}: missing ${label}`);
  }
  for (const heading of ['Outcome', 'Entry conditions', 'Steps', 'Acceptance criteria', 'Evidence to retain']) {
    check(body.includes(`## ${heading}\n`) || body.includes(`## ${heading}\r\n`), `${id}: missing ${heading}`);
  }
  check(/^- \[[ x]\] .+/m.test(body), `${id}: missing acceptance checklist`);
  const entries = body.split('## Entry conditions')[1]?.split('\n## ')[0] || '';
  const milestone = Number(id.match(/^M(\d+)/)[1]);
  const gates = milestone === 2 ? ['G1', 'M1'] : milestone >= 3 && milestone <= 7 ? ['G1', 'G2'] : id === 'M8.11' ? ['G4'] : milestone === 8 ? ['G3'] : [];
  for (const gate of gates) check(new RegExp(`\\b${gate}\\b`).test(entries), `${id}: missing entry gate ${gate}`);
  for (const decision of field(body, 'Open decisions').match(/\bD\d\d\b/g) || []) {
    check(master.includes(`| ${decision} |`), `${id}: undefined decision ${decision}`);
  }
  for (const boundary of field(body, 'Public boundaries').match(/\bT\d\d\b/g) || []) {
    check(master.includes(`| ${boundary} |`), `${id}: undefined public boundary ${boundary}`);
  }
}
function requirements(text) {
  const expanded = text.replace(/FR-(\d\d)[–-]FR-(\d\d)/g, (_, low, high) =>
    Array.from({ length: Number(high) - Number(low) + 1 }, (__, i) => `FR-${String(Number(low) + i).padStart(2, '0')}`).join(' '));
  return [...new Set(expanded.match(/FR-\d\d|PRD \d+(?:\.\d+)?|PRD success criteria|\bQ\d+\b/g) || [])];
}
const indexed = new Set();
for (const row of index.split(/\r?\n/).filter(line => /^\| \[M\d+\.\d+ —/.test(line))) {
  const cells = row.split('|');
  const id = ids(cells[1])[0];
  const task = tasks.get(id);
  check(task && !indexed.has(id), `${id}: missing file or duplicate index row`);
  indexed.add(id);
  if (!task) continue;
  check(cells[1].includes(`](${task.filename})`), `${id}: index points to a different file`);
  check(same(ids(cells[2]), task.dependencies), `${id}: index/task dependencies differ`);
  check(same(requirements(cells[3]), requirements(field(task.body, 'Requirements'))), `${id}: index/task requirements differ`);
}
check(same(indexed, tasks.keys()), 'Task files and index rows do not have one-to-one coverage');
for (const match of master.matchAll(/^\| M(\d) \| .* \| (\d+) \|$/gm)) {
  const count = [...tasks.keys()].filter(id => id.startsWith(`M${match[1]}.`)).length;
  check(count === Number(match[2]), `M${match[1]}: master count differs from task files`);
}
const visited = new Set();
const visiting = new Set();
let dependencyEdges = 0;
function visit(id) {
  if (visiting.has(id)) { errors.push(`Dependency cycle at ${id}`); return; }
  if (visited.has(id)) return;
  visiting.add(id);
  for (const dependency of tasks.get(id).dependencies) {
    dependencyEdges++;
    if (!tasks.has(dependency)) errors.push(`${id}: unknown dependency ${dependency}`);
    else visit(dependency);
  }
  visiting.delete(id);
  visited.add(id);
}
for (const id of tasks.keys()) visit(id);
let diagrams = 0;
for (const filename of ['ARCHITECTURE.md', 'plan/MASTER_PLAN.md']) {
  const file = path.join(root, filename);
  for (const match of read(file).matchAll(/```mermaid\r?\n([\s\S]*?)```\s*\n\s*\[SVG\]\(([^)]+)\)[\s\S]*?\[Mermaid source\]\(([^)]+)\)/g)) {
    diagrams++;
    const source = path.resolve(path.dirname(file), match[3]);
    const svg = path.resolve(path.dirname(file), match[2]);
    check(fs.existsSync(source) && read(source).trim().replace(/\r\n/g, '\n') === match[1].trim().replace(/\r\n/g, '\n'), `${filename}: Mermaid source differs: ${match[3]}`);
    check(fs.existsSync(svg) && /^<svg\b/.test(read(svg).trim()), `${filename}: missing or empty SVG ${match[2]}`);
  }
}
check(diagrams === fs.readdirSync(path.join(root, 'docs/diagrams/source')).filter(name => name.endsWith('.mmd')).length, 'Standalone/embedded diagram counts differ');
const skills = JSON.parse(read(path.join(root, 'skills-lock.json'))).skills;
for (const [name, metadata] of Object.entries(skills)) {
  check(fs.existsSync(path.join(root, '.agents/skills', name, 'SKILL.md')), `${name}: locked skill is missing`);
  check(metadata.sourceType === 'github' && metadata.source && metadata.skillPath && /^[a-f\d]{64}$/.test(metadata.computedHash), `${name}: incomplete installer metadata`);
}
const prdHash = crypto.createHash('sha256').update(fs.readFileSync(path.join(root, 'PRD.md'))).digest('hex');
const expectedHash = process.argv[2];
if (expectedHash) check(expectedHash.toLowerCase() === prdHash, 'PRD bytes differ from the supplied pre-review hash');
console.log(JSON.stringify({
  result: errors.length ? 'FAIL' : 'PASS',
  documents: documents.length, localLinks, anchorLinks, tasks: tasks.size,
  dependencyEdges, diagrams, lockedSkills: Object.keys(skills).length,
  prdSha256: prdHash, prdComparedWithBaseline: Boolean(expectedHash), errors,
}, null, 2));
process.exitCode = errors.length ? 1 : 0;

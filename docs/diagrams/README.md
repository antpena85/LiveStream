# LiveStream planning diagrams

The [master plan](../../plan/MASTER_PLAN.md) and
[software architecture](../../ARCHITECTURE.md) contain embedded Mermaid blocks.
The `source/` directory contains matching standalone `.mmd` files. The
`rendered/` directory contains SVG exports for viewers without Mermaid support.
All diagrams describe a proposed design, except where the documents identify
an approved PRD requirement or decision.

| Diagram | Editable source | Rendered SVG |
| --- | --- | --- |
| Delivery roadmap | [delivery-roadmap.mmd](source/delivery-roadmap.mmd) | [delivery-roadmap.svg](rendered/delivery-roadmap.svg) |
| System context | [system-context.mmd](source/system-context.mmd) | [system-context.svg](rendered/system-context.svg) |
| Phone components | [phone-components.mmd](source/phone-components.mmd) | [phone-components.svg](rendered/phone-components.svg) |
| Media pipeline | [media-pipeline.mmd](source/media-pipeline.mmd) | [media-pipeline.svg](rendered/media-pipeline.svg) |
| Match model | [match-model.mmd](source/match-model.mmd) | [match-model.svg](rendered/match-model.svg) |
| Match lifecycle | [match-lifecycle.mmd](source/match-lifecycle.mmd) | [match-lifecycle.svg](rendered/match-lifecycle.svg) |
| Remote command and retry | [remote-command.mmd](source/remote-command.mmd) | [remote-command.svg](rendered/remote-command.svg) |
| Stream lifecycle | [stream-lifecycle.mmd](source/stream-lifecycle.mmd) | [stream-lifecycle.svg](rendered/stream-lifecycle.svg) |

## Rendering tool and provenance

- Skill: [pretty-mermaid](../../.agents/skills/pretty-mermaid/SKILL.md),
  installed only in this project's `.agents/skills/pretty-mermaid` directory.
- Source: [imxv/pretty-mermaid-skills](https://github.com/imxv/pretty-mermaid-skills).
  Discovery on 2026-10-17 reported approximately 4.4K skill installs and 1.5K
  repository stars. Instructions, references, package manifest, and rendering
  scripts were reviewed for this documentation use.
  [Skills directory entry](https://skills.sh/imxv/pretty-mermaid-skills/pretty-mermaid).
- Installer-maintained source/integrity metadata:
  [skills-lock.json](../../skills-lock.json).
- Renderer: `beautiful-mermaid`, through the skill's bundled CLI.
- Output: SVG, theme `github-light`, default spacing, consistent across all
  eight diagrams. PNG previews were also rendered temporarily for inspection.
- Runtime: temporary Node.js 22; the global Node.js installation was not changed.
- Dependencies live inside the skill directory. No application package or
  application build workflow was created.

## Update and regenerate

Edit the relevant document's Mermaid block and its matching `.mmd` file together.
Keep their content identical. Then run this documentation command from the
project root in PowerShell:

```powershell
npx --yes --package=node@22 node .agents/skills/pretty-mermaid/scripts/batch.mjs --input-dir docs/diagrams/source --output-dir docs/diagrams/rendered --format svg --theme github-light --workers 4
```

This is the rendering command used for these artifacts; it is not an application
build or test command. It can download the temporary runtime if needed. If the
skill's dependencies are missing, install them in the skill directory:

```powershell
Push-Location .agents/skills/pretty-mermaid
npm install --ignore-scripts --no-fund --no-audit
Pop-Location
```

For visual inspection, the same bundled renderer supports `--format png` and
`--width 1800`; use a temporary output directory. Review labels, arrow direction,
branch conditions, and readability after changes. A successful render validates
diagram syntax, not feasibility or implementation of the proposed system.

## Validation of this planning package

- All eight Mermaid sources rendered successfully as SVG and PNG.
- SVGs were nonempty and parsed as valid XML.
- Embedded Mermaid blocks matched their standalone sources.
- Local document and artifact links were checked.
- Requirements coverage and open decisions were reviewed against the approved PRD.
- The approved PRD was preserved byte for byte.

Application and physical-device validation remain future work, as specified in
the [master plan](../../plan/MASTER_PLAN.md#6-validation-and-evidence-plan).

The [M1.1 review on 2026-10-18](../../plan/reviews/m1-1-planning-package-review.md#mechanical-and-diagram-evidence)
rechecked all eight embedded/source pairs, parsed the SVGs, rendered temporary
SVG/PNG copies, and visually inspected the PNGs. Retained SVGs matched fresh
renders byte for byte. This review used the existing Node.js v16.14.0 runtime
and installed skill dependencies; no diagram source or export changed.

## M1.6 scoreboard layout illustration

The [scoreboard specification](../../plan/specifications/m1-6-overlay-display-specification.md)
includes an editable [SVG illustration](source/scoreboard-layout.svg) and
[PNG preview](rendered/scoreboard-layout.png). This hand-authored SVG is separate
from the eight Mermaid pairs above. It shows two schematic 1280×720 frames
with identical match values at top-left and bottom-center, illustrating the
owner's configurable placement requirement. Guides and annotations are
documentation content, not broadcast fields. Sizes, margins, and colors remain
proposals; static artwork cannot establish encoded/received readability.

The PNG was rendered with the existing Node.js runtime and the already installed
`@resvg/resvg-js` dependency under the pretty-mermaid skill. No new dependency
was installed. To reproduce from the project root in PowerShell:

```powershell
$renderLayoutCode = @'
const fs = require('fs');
const { Resvg } = require('./.agents/skills/pretty-mermaid/node_modules/@resvg/resvg-js');
const svg = fs.readFileSync('docs/diagrams/source/scoreboard-layout.svg', 'utf8');
const png = new Resvg(svg, { font: { loadSystemFonts: true } }).render().asPng();
fs.writeFileSync('docs/diagrams/rendered/scoreboard-layout.png', png);
'@
$renderLayoutCode | node
```

The 1280×1680 PNG was visually inspected on 2026-10-18 for required fields,
labels, and clipping. This validates the documentation illustration only.

## D11 match lifecycle update, 2026-10-20

Updated the embedded architecture diagram and matching
[match lifecycle source](source/match-lifecycle.mmd) for PRD Q22–Q25. A cap
alone no longer produces a potential winner. The cap-conflict path requires
explicit continuation for that set or a correction; a valid winner still
requires the configured target and margin, followed by set confirmation.
The exception does not carry into later sets. Other open policies in the
surrounding specification remain proposals.

Regenerated [match-lifecycle.svg](rendered/match-lifecycle.svg) with the existing
Node.js runtime, installed pretty-mermaid tooling, and `github-light` theme.
A temporary 1800-pixel PNG was inspected for states, labels, arrows, and clipping;
all labels are present. The graph is dense, so the surrounding policy prose
remains necessary. These checks validate documentation, not application behavior.

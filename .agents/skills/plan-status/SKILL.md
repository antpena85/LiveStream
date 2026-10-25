---
name: plan-status
description: Report LiveStream plan progress from its PRD, master plan, task plans, and retained evidence. Use for plan status, milestone progress, blockers, next tasks, or a refreshed Markdown plan status dashboard.
---

# Plan status

Produce an evidence-backed view of the current plan. For a dashboard or refresh
request, create or update `plan/STATUS.md` and give a concise chat summary with
a link. For an ordinary status question, report in chat without changing files
unless saving was requested. This is a documentation workflow.

## Read the current sources

Resolve paths from the project root containing `AGENTS.md`; a Git repository
is not required. Read these files afresh on every invocation:

- `AGENTS.md`: current phase and authorization limits.
- `PRD.md`: approved product requirements, confirmed answers, and open details.
- `plan/MASTER_PLAN.md`: milestones, G0 onward, decision register, and next work.
- `plan/subplans/README.md`: task membership, dependencies, and coverage.
- Each indexed task plan: `Task status`, entry conditions, acceptance checklist,
  and evidence references. Follow retained review/inventory/evidence links for
  tasks reported complete or changed. Use `ARCHITECTURE.md` when design status
  affects the report.

Use an existing `plan/STATUS.md` only to compare snapshots. It is derived output,
not evidence for current completion. Read linked artifacts before claiming they
establish a result. Do not browse to revalidate platform facts merely to report
documented status; research is needed if the user also requests new fact checks.

## Assess progress and readiness separately

- Count each unique indexed task once. Check index membership against actual
  task files and milestone totals; flag missing files or duplicate IDs.
- Normalize explicit task states as Complete, In progress, Not started, or
  Unverified. Unknown or contradictory states remain Unverified. Missing evidence
  makes a completion claim Unverified; keep the source's claim visible.
- A plan file, unchecked acceptance list, proposed design, or completed review
  does not establish delivered application behavior. Use explicit completion
  records and their evidence; do not infer work from file timestamps.
- Calculate task counts per milestone and reconcile their sum with the overall
  counts. If showing a percentage, label it as unweighted task completion, not
  effort, schedule, implementation, or release readiness. Prefer counts.
- Evaluate readiness using both task dependencies and entry gates from the
  master plan and task files. Numeric order alone is not a dependency.
  Distinguish work available now from work waiting for owner inputs, physical
  measurements, implementation authorization, or earlier tasks.
- Report each gate using its documented evidence and remaining conditions.
  Approval of the PRD does not approve proposed architecture or open G1.
- Count unresolved decision records from the current register. Partial evidence
  does not resolve the record. In particular, M1.2 can be complete while D01
  remains open for installed builds, accessories, uploads, and device evidence.
- Keep confirmed Q7 direct watch pairing and confirmed TDD separate from open
  framework/tool choices and unconfirmed T01–T06 public boundaries.
- Treat owner reports, documentation review, automated results, physical checks,
  and received-broadcast evidence as different evidence. Do not substitute one
  for another when assessing an acceptance criterion.

If current sources conflict, show the discrepancy with links to both and label
the affected assessment Unverified. An explicitly dated later update may explain
an older review's historical state. Do not rewrite source plans to make a report
consistent. State missing evidence and continue reporting unaffected work.

## Build the dashboard

Keep the saved report compact, using relative Markdown links. Include:

- An as-of date in the user's timezone, sources reviewed, current milestone,
  and a plain statement of implementation/release status.
- Summary counts: complete, in progress, not started, unverified, total, and
  unresolved decisions. Omit zero categories only if the total is still clear.
- A milestone table with milestone, complete/total, status, and the next unmet
  condition. Link each milestone to its source.
- Completed work with retained evidence links, including limitations that remain
  material to readiness.
- A gate table with gate, status, and the evidence or action still needed.
- The most useful next tasks, linking to their task plans and naming external
  inputs separately from documentation that can proceed now. Recommendations
  must be marked as such; do not execute the next task during a status request.
- Open decision groups with IDs and links, and a brief evidence-gap note where
  the counts might otherwise suggest more certainty than the sources support.
- Changes since the previous snapshot only when a previous snapshot exists and
  source evidence supports the comparison. Do not invent progress history.

Refresh the generated snapshot while preserving separately labeled owner notes.
Do not add a history log, scripts, a website, hosting, or application dependencies
unless the user requests those capabilities. Do not close decisions, change
acceptance checkboxes, or start application work as a side effect of reporting.

Before finishing, reconcile counts, verify local links, and check every claimed
completion and gate result against the retained evidence. Use the existing
`node plan/tools/check-planning-package.cjs` when available for documentation
links and task-graph consistency; it does not validate completion evidence or
dashboard totals, which still need review. A status report does not require
application tests.

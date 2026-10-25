# D11 completed-match entry policy

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Owner decision confirmed; written cases apply the selected policy.  
Decision: [D11.3](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[PRD Q23](../../PRD.md#53-confirmed-scope-decisions)  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Scope

Q23 permits entering a totals-only baseline for an ongoing match when earlier
scores are unknown. It deliberately leaves a match that has already finished
at initial entry unresolved. This review concerns only that completed-start
case. It does not change the confirmed rule for manual corrections after a
match has been played in LiveStream.

The PRD requires final-set points to remain visible after match completion,
explicit confirmation for normal live progression, and no invented earlier
scores or winner order. The selected policy must preserve those requirements.

## Owner decision recorded on 2026-10-21

The owner confirmed that completed-match entry is allowed only when the operator supplies a complete ordered
set history with valid scores under the selected preset. Derive totals from
that history; do not accept totals-only completed entry. The completed state
shows its final recorded set and points, has no active server or timeouts, and
does not enter an active next set. A phone/tablet completes this setup; the
watch remains view-only for history and starting values.

This lets the owner show a finished match that is fully known, without asking
the app to make unsupported inferences. It also keeps a mid-match baseline
separate from verified history.

The alternative considered was to prohibit all completed-match entry. Operators would enter only
ongoing matches or complete a match through the regular in-app confirmation
flow. This is simpler, but it cannot represent an already-finished match when
the full score sheet is available.

## Written review cases

The following cases apply the confirmed Q28 policy. They are planning examples,
not executable tests or delivered behavior.

| ID | Starting input | Action | Expected result under the confirmed policy |
| --- | --- | --- | --- |
| C01 | Best-of-3; ordered set history: A 25–20, B 25–22, A 15–12 | Create a completed match | Accept. Derive totals 2–1, retain set 3 at 15–12 as final points, mark Complete, keep no active set/server/timeouts. |
| C02 | Best-of-5; ordered history: A 25–21, A 25–22, A 25–19 | Create a completed match | Accept. Derive 3–0 and Complete; do not create empty set 4. |
| C03 | Best-of-3; starting totals 2–1 but no set scores/order | Create a completed match | Reject with a request for complete ordered results. Do not invent the deciding-set score or winner order. |
| C04 | Best-of-3; ordered history: A 25–20, A 25–22, B 15–13 | Create a completed match | Reject. A reached two wins before set 3, so later play is inconsistent. Preserve the draft for correction but commit no match. |
| C05 | Best-of-5; ordered history: A 25–20, B 25–22, A 25–21, B 25–23, A 15–13 | Create a completed match | Accept. Use the configured deciding-set target for set 5; derive 3–2 and retain the final 15–13. |
| C06 | Target 25, margin 2, cap 30; history ends set 1 at 30–29 without retained continuation evidence | Create a completed match | Reject the draft as unresolved cap conflict. The selected current-set continuation cannot be invented from the final score. |
| C07 | A valid completed match was entered under C01 | Change a completed-set result | Use the separately decided historical-correction interaction. Recompute only after validation; never silently delete later sets or start streaming. |

## Boundaries retained

- A totals-only baseline remains permitted only for an ongoing match under Q23.
  It supplies no fictional scores, order, server, or completed-match result.
- Normal live scoring still pauses at a potential winner for confirmation. The
  proposed entry flow validates an already-recorded history; it does not make
  ordinary in-app set/match confirmation automatic.
- The ordinary phone/tablet control matrix governs entry and history changes.
  Q27 does not grant the watch those actions merely because it can Undo an
  allowed latest scoring edit.
- Reset, first-server selection for a newly active set, cap-input restoration,
  history-correction interaction, persistence, and receipts stay separate D11
  or technical decisions.

## Handoff

[M3.1](../subplans/m3-1-match-setup-and-presets.md) needs the selected policy
to validate initial match setup. [M3.6](../subplans/m3-6-historical-set-corrections.md)
uses the distinction between entered complete history and an ongoing baseline.
Both tasks remain Not started and require G1/G2 plus public-boundary
confirmation before implementation or tests.

Q28 is recorded in the PRD and the planning handoffs are synchronized. This
decision neither closes D11 nor opens an implementation gate.

## Documentation validation

Validated after recording Q28 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 70 Markdown documents,
1,104 local links, 257 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills.

Separate reconciliation confirmed unique Q1–Q28 and unique sequential C01–C07.
Task counts remain 7 complete / 1 in progress / 45 not started, with 13 aggregate
decision records still open. These documentation checks do not establish
application behavior, physical-device support, provider integration, or a
received broadcast.

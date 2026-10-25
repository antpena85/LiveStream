# D11 historical-correction and reopening policy

Prepared: 2026-10-21 (America/New_York)  
Status: Confirmed by the owner on 2026-10-21 as PRD Q30.  
Decision: [D11.5](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[PRD Q17](../../PRD.md#53-confirmed-scope-decisions)  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Scope

Q17 rejects a historical correction that makes later recorded play inconsistent;
LiveStream keeps the recorded match intact rather than deleting later sets.
This review addresses the opposite case: a complete match correction that keeps
the ordered history valid but leaves neither team with enough set wins.

The question is whether that valid correction reopens the match for its required
next set. It does not authorize destructive removal of later recorded sets,
editing history from the watch, automatic streaming, or an Undo beyond Q26.

## Confirmed decision

Review historical corrections on a phone or Android tablet as one coherent draft.
Show the corrected set history, derived totals, resulting phase, and any required
next set before committing. Reject an invalid result and preserve the committed
match unchanged, as Q17 requires.

When a valid correction to a completed match leaves neither team at the required
sets-won threshold, reopen it at the next required set with points 0–0, the full
configured timeout allowance, no active server, and the existing team identities,
configuration, display order, and corrected history preserved. The operator
selects the first server before rally input. Streaming remains independent.

This permits a genuine score-sheet correction without forcing a separate match
or inventing a new score. It also keeps correction review deliberate and makes
the reopening state visible before it is accepted.

## Written review cases

These cases apply the confirmed policy. They are planning examples, not executable
tests or delivered behavior.

| ID | Starting state | Action | Expected result under the recommendation |
| --- | --- | --- | --- |
| H01 | Best-of-3 complete 2–0: A won 25–20 and 25–21 | Correct set 2 to B 25–21, preview, then commit | Accept coherent correction. Totals become 1–1; reopen for set 3 at 0–0 with full timeouts and no active server. Preserve both corrected scores and display order. |
| H02 | Best-of-5 complete 3–1: A won sets 1, 3, 4; B won set 2 | Correct A’s set 4 score without changing its winner | Accept; remain Complete 3–1 with final set points updated. Do not reopen. |
| H03 | Best-of-3 complete 2–1: A won sets 1 and 3; B won set 2 | Change set 1 winner to B while retaining set 3 | Reject: B would have won 2–0 before set 3, so later play becomes inconsistent. Preserve committed match. |
| H04 | Best-of-3 complete 2–0: A won sets 1 and 2 | Change set 2 to B 25–23, then cancel the review | Preserve the original 2–0 complete match. No partial totals/history/phase change occurs. |
| H05 | H01 has reopened at set 3 with no active server | Attempt a rally before selecting a server | Reject or require explicit server selection; do not infer service from old final-set data. |
| H06 | H01 has reopened, stream currently active | Commit the correction | Match state changes only. The stream stays in its current state and does not restart or stop. |

## Boundaries retained

- Only phone/tablet history controls may initiate or commit this correction.
  The watch is view-only for history and can never bypass that restriction via Undo.
- A correction draft is not a scoreboard update. The accepted correction changes
  history, totals, phase, and active-set values atomically after review.
- This confirmed Q30 policy allocates the first server only for a match reopened
  by completed-history correction. The later [serve/reset review](d11-new-set-serve-and-reset-policy-review.md)
  records Q33–Q35: ordinary new sets and confirmed reset also require explicit
  first-server selection; reset contents/authority are confirmed separately.
- Q28 completed-match setup is separate: it validates supplied history at entry.
  This review changes already-stored, complete history after a match.
- Revision/receipt implementation details remain D04/D11 technical work.

## Handoff

[M3.6](../subplans/m3-6-historical-set-corrections.md) needs this policy to
commit coherent corrections. [M3.4](../subplans/m3-4-set-and-match-confirmation.md)
uses the result phase, and [M3.2](../subplans/m3-2-rally-points-and-serve.md)
uses the required active-server state. These tasks remain Not started and require
G1/G2 plus public-boundary confirmation before implementation or tests.

The decision is recorded in PRD Q30 and synchronized with M1.5, task handoffs,
architecture, and the status dashboard. It neither closes D11 nor opens an
implementation gate.

# D11 starting-baseline and history reconciliation

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Both owner decisions confirmed as PRD Q31/Q32; written cases apply the selected policy.  
Decision: [D11.3 and D11.5](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[PRD Q17/Q23/Q28/Q30](../../PRD.md#53-confirmed-scope-decisions)  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Scope and confirmed constraints

Q23 allows an ongoing match to start from sets-won totals when earlier scores
are unknown. Those totals establish neither earlier scores nor winner order.
Q17 rejects corrections that invalidate later play. Q28 separately requires
complete valid history when entering an already-finished match at setup.
Q30 permits a valid reviewed correction to reopen a completed match when
neither team still has enough set wins.

This review concerns replacing unknown early history with supplied results and
correcting an incorrectly entered baseline. It continues the existing M1.5
area using the installed PRD and volleyball-rules guidance. No platform fact,
new skill, storage schema, or application behavior is established here.

## Owner decisions recorded on 2026-10-21

Both questions provided alternatives and the interface's custom-answer field.
The owner selected the recommended answer to each. These are recorded as
[PRD Q31/Q32](../../PRD.md#53-confirmed-scope-decisions).

| Choice | Recommended | Alternative | Answer |
| --- | --- | --- | --- |
| Earlier scores become known | Replace the entire baseline with complete, ordered, valid early-set history whose wins match that baseline. Preserve later play and count each set once. | Keep the baseline fixed and leave early scores unrecorded. This reduces correction controls. | Confirmed: recommended answer, Q31. |
| Incorrect starting totals after later play | Allow a reviewed phone/tablet correction only when the resulting sequence remains valid. Reject changes that invalidate or discard later play. | Lock starting totals after the first later set is confirmed; earlier mistakes require a new match setup. | Confirmed: recommended answer, Q32. |

## Confirmed policy and validation consequences

The following rules apply Q31/Q32 alongside the existing confirmed policies.
Revision, identity, storage, and atomic-commit mechanisms remain technical
decisions; these rules do not select their implementation.

1. Displayed sets won equal the unknown-history baseline plus wins in recorded
   confirmed results. Each completed set contributes once. For an ongoing
   match, the active set number is one plus the total number of completed sets.
   A completed match retains its last recorded set and points.
2. Keep unknown early history explicitly unknown. Do not assign fictional point
   scores or winner order. Each team's ongoing baseline must remain below the
   match-winning threshold; validate the known later sequence against it.
3. When supplying earlier scores, replace the entire baseline only with a
   complete ordered prefix. Validate each score under that set's applicable
   target, margin, and cap policy; its wins must reproduce the existing baseline.
   Remove the baseline contribution as those results are recorded. Do not append
   the prefix while retaining its baseline contribution or accept a partial
   replacement in this flow.
4. A mismatching prefix cannot silently overwrite starting totals. Under Q32,
   use a coherent baseline correction review that shows the
   intended totals and all consequences. Validate the proposed sequence before
   committing any part of it.
5. When correcting baseline totals, show derived totals, set numbers, applicable
   regular/deciding targets, and resulting phase before confirmation. A changed
   baseline count can shift later set numbers; preserve the ordered recorded
   results and existing current play. Reject a draft if a recorded result becomes
   invalid or either team would already have won before later recorded play.
   Never remove later play or alter a known score merely to make a draft pass.
6. A valid ongoing correction preserves current points, server, timeout counts,
   team identities, configuration, and display order. Re-evaluate the active
   set under its applicable target and cap rules; if it is a potential winner
   or unresolved cap conflict, keep ordinary rally input paused under Q24/Q29.
   A correction does not automatically confirm the current set.
7. If a valid correction removes a completed match's winning total, apply Q30:
   reopen the required next set at 0–0 with full configured timeouts and no
   active server until selected. This also covers a match completed through
   live confirmations after a totals-only baseline; Q28's separate full-history
   requirement for completed entry at setup remains unchanged.
8. Preview and commit the accepted correction together on a phone/tablet.
   Cancellation or rejection preserves the committed match and its Undo
   opportunity. An accepted baseline/history correction follows Q26's latest-edit
   Undo rule; Q27 requires a phone to undo it. Streaming remains independent.

Partial-history replacement and a general editor for preset configuration are
not included by these answers. An accepted correction changes no preset and
does not authorize deleting later play.

## Written review cases

These cases apply the confirmed Q31/Q32 policy and existing linked decisions.
They are documentation examples, not executable tests or delivered behavior.
Scores use stable A–B identities; a named winner's score is written winner first.
Unless stated otherwise, use best-of-5, targets 25/15, margin 2, no cap, and two
timeouts per team.

| ID | Starting state | Action | Expected result under the confirmed policy |
| --- | --- | --- | --- |
| B01 | Baseline 1–1; recorded set 3 won by A 25–20; active set 4 at 8–6, serve B, timeouts 1–2 | Display totals and history | Show totals 2–1, unknown early scores/order, recorded set 3, and unchanged set 4 values. Do not treat 2–1 as another baseline. |
| B02 | B01 | Supply set 1 A 25–21 and set 2 B 25–22; review and commit | Replace baseline 1–1 with these two results. Totals remain 2–1; recorded set 3 and active set 4 retain their numbers and values. Each set counts once. |
| B03 | B01 | Supply two early A wins as a matching-history replacement | Reject: that prefix yields 2–0, not baseline 1–1. Do not silently change the baseline. A distinct reviewed correction must also validate all later play. |
| B04 | B01 | Supply only set 1 A 25–21 as the entire prefix | Reject this incomplete replacement. Keep baseline 1–1; do not guess the missing score/order or count the supplied set twice. |
| B05 | Baseline 1–1; recorded set 3 A 25–20; active set 4 at 8–6, serve B, timeouts 1–2 | Correct baseline to 0–2; review and commit | Accept: derived totals become 1–2, and current set 4 remains necessary. Preserve its points/server/timeouts and recorded result. Earlier scores/order remain unknown. |
| B06 | Baseline 1–0; recorded set 2 B 25–20; active set 3 at 7–5, serve A, timeouts 2–1 | Correct baseline to 1–1; review all affected numbers and commit | Accept: the preserved recorded result becomes regular set 3; the active set becomes regular set 4 at 7–5. Totals become 1–2. Show both numbering changes before acceptance. |
| B07 | B01 | Correct baseline to 2–0 | Reject: A's recorded win would complete a 3–0 match before the existing set 4 play. Preserve all committed data; do not discard set 4. |
| B08 | Best-of-3 completed in-app from baseline 1–1 and recorded deciding set 3 A 15–12 | Correct baseline to 1–0 | Reject: the preserved recorded result would become regular set 2, where 15–12 does not meet the 25-point target. Do not reinterpret its score or invent a replacement result. |
| B09 | B01; a correction preview changes the baseline | Cancel review | Preserve baseline, history, totals, current play, and the prior Undo opportunity. The preview never updates the broadcast scoreboard. |
| B10 | Completed in-app from baseline 1–0, recorded sets 2 and 3 both A wins, final points 25–20 | Correct baseline to 0–1; review and commit | Accept coherent totals 2–1 and apply Q30: reopen set 4 at 0–0 with full timeouts and no server until selected. Keep the recorded results; do not require invented early scores or start/stop streaming. |
| B11 | B06 is the latest accepted edit; no later match edit | Undo from a phone; separately attempt it from the watch | Phone restores the prior baseline, result numbering, totals, and active-set state once under Q26. Watch directs the operator to a phone and changes nothing under Q27. |
| B12 | Baseline 1–1 with later play; configured cap 30, margin 2 | Supply matching early winners but one claimed completed result is 30–29 | Reject: matching win totals cannot validate a score without the required margin. Do not infer continuation or waive cap/margin checks to accept the prefix. |

## Handoff and remaining work

- [M3.1](../subplans/m3-1-match-setup-and-presets.md) consumes baseline entry and
  full-history validation. [M3.6](../subplans/m3-6-historical-set-corrections.md)
  consumes the selected reconciliation policy and coherent correction examples.
- [M3.4](../subplans/m3-4-set-and-match-confirmation.md) consumes phase/target
  reevaluation; [M3.5](../subplans/m3-5-undo-and-confirmed-reset.md) consumes the
  confirmed Undo scope without granting watch history-edit permission.
- [M3.7](../subplans/m3-7-atomic-persistence-and-restart.md) and
  [M6.2](../subplans/m6-2-ordered-idempotent-commands.md) still need atomic state,
  Undo, revision, receipt, and stale-review handling. No representation is selected.
- The later [serve/reset review](d11-new-set-serve-and-reset-policy-review.md)
  records Q33–Q35 for ordinary first-server selection and reset contents/authority.
  The later [cap-permissions review](d11-cap-continuation-permissions-review.md)
  records Q36's phone/tablet-only confirmation and latest-confirmation Undo.
  Technical command retention and identity/revision/storage mechanisms remain open.

M1.5 remains complete for documentation. Q31/Q32 settle the two owner policy
choices without changing task counts, closing D11, confirming public test
boundaries, or opening G1/G2.

## Pre-answer documentation validation

Validated on 2026-10-21 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 73 Markdown documents,
1,149 local links, 265 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills.

Separate reconciliation confirmed unique sequential B01–B12 and Q1–Q30, two
pending answers, unchanged task counts of 7 complete / 1 in progress / 45 not
started, and 13 open aggregate decision records. Completed tasks retain checked
acceptance lists; not-started tasks retain unchecked lists. AGENTS.md and
skills-lock.json match their pre-work SHA-256 hashes.

Manual review checked single counting, preserved current play, explicit
set-number/target changes, earliest match finish, deciding-set validation,
cancellation, Q30 reopening, and Q26/Q27 Undo permissions. Focused local skill
discovery confirmed the existing PRD/volleyball guidance covers this continuation;
no fresh online discovery or upstream integrity check is claimed. These are
documentation checks, with no application tests or physical/provider results.

## Post-answer synchronization and validation

The owner selected both recommended answers on 2026-10-21. Q31/Q32 now record
matching complete-history replacement without double counting and valid reviewed
starting-total corrections after later play. The PRD acceptance criteria, M1.5
register/examples, affected task handoffs, architecture, master plan, index, and
status dashboard are synchronized. Remaining D11 server/reset/controller and
technical choices stay open.

The documentation checker passed after synchronization: 73 Markdown documents,
1,151 local links, 268 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills; exit code 0, no errors.
Separate reconciliation confirmed unique Q1–Q32 and B01–B12, fourteen confirmed
D11 owner choices, unchanged task counts of 7 complete / 1 in progress / 45 not
started, and 13 open aggregate decision records. No not-started acceptance box
was checked; AGENTS.md and skills-lock.json retain their pre-work hashes.

These checks establish documentation consistency. Application behavior,
physical-device support, provider integrations, and broadcasts remain untested;
G1/G2 and public-boundary confirmation retain their existing requirements.

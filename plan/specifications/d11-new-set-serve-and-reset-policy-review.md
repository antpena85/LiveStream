# D11 new-set serve and match-reset policy

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Three owner decisions confirmed as PRD Q33–Q35; technical mechanisms remain open.  
Decision: [D11.6](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[device control matrix](../../PRD.md#24-device-control-matrix),
[Q18/Q24/Q26/Q27/Q30](../../PRD.md#53-confirmed-scope-decisions)  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Scope and established constraints

Ordinary set progression needs an explicit result confirmation. The PRD requires
deliberate match reset, stable team ownership, full configured timeouts for a
new set, and retained final points at match completion. Q26 excludes confirmed
reset from Undo; Q27 limits watch Undo to its allowed match actions. Q30 already
requires no server until explicit selection when a historical correction reopens
a completed match. Those confirmed policies remain in force.

This review addresses the first server for an ordinary new set, what a reset
retains, and which device roles may reset. It does not authorize application
implementation or a change to streaming, accounts, saved presets, or pairing.
No player rotation, automatic side swap, or coin-toss workflow is added.

## Owner decisions recorded on 2026-10-21

Each question provided recommended and alternative answers plus the interface's
custom-answer field. The owner selected all three recommended answers, now
recorded as [PRD Q33–Q35](../../PRD.md#53-confirmed-scope-decisions).

| Choice | Recommended | Alternative | Answer |
| --- | --- | --- | --- |
| First server for each new set | Start without a selected server; require an explicit team selection before ordinary rally scoring. Avoid carrying forward an incorrect server. | Carry forward the previous set's last serving team, with manual override. This reduces setup taps. | Confirmed: recommended answer, Q33. |
| Confirmed reset contents | Keep team identities/names/codes, the match's copied settings, and current display order. Clear points, sets-won baseline, completed history, Undo, and cap-continuation approval; restore full timeouts and leave serve unselected. | Clear the match and return to setup, requiring teams, settings, and first server to be entered again. | Confirmed: recommended answer, Q34. |
| Reset authority | Permit the streaming phone and authorized second Android phone/tablet, with explicit confirmation; keep reset unavailable on the watch. | Streaming phone only; or all three roles including the watch, with explicit confirmation on each. | Confirmed: recommended answer, Q35. |

## Confirmed serve behavior

These consequences apply Q33 together with the existing confirmed policies.

- An ordinary new set starts at 0–0 with full configured timeouts and no selected
  server. Do not derive its first server from the preceding set's last rally,
  winning team, current display side, or an assumed alternating order.
- Show the unset state on phones/watch and omit the active serving marker from
  the scoreboard while it is unset. The operator explicitly chooses stable team
  A or B using the existing permitted serve control. Exact UI remains proposed.
- Reject ordinary rally input until a serving team is selected; do not use that
  first point to silently fill the missing server. Apply this rule on the
  authoritative phone even when a controller displays an outdated enabled button.
- Selecting the first server is an accepted serve edit under Q26/Q27, available
  through the already-approved serve controls on each authorized device role.
  Undo of that latest selection restores the unset state and its rally block.
- Existing correction controls remain available; selecting a server grants no
  permission to edit names/history or to confirm cap continuation from the watch.
- A mid-match setup or restored ongoing set retains its explicitly supplied or
  durably recorded server. Matching-history replacement and valid baseline
  correction under Q31/Q32 preserve existing current play and server. The fresh
  new-set policy does not clear them merely because the app restarts.
- Completing the match creates no next set and no new first-server prompt.
  Q30's reopened-match set also requires explicit server selection.

The carry-forward alternative was not selected. Initial and subsequent fresh
sets, confirmed reset, and Q30 reopening all require explicit server selection;
none may infer it from earlier play.

## Confirmed reset behavior and authority

These consequences apply Q34/Q35 together with the established Q26 Undo boundary.

- Preview the reset's effect and obtain deliberate confirmation on an authorized
  phone/tablet. Canceling changes no committed match values or Undo opportunity.
  Opening the reset prompt is not an accepted match edit.
- Retain both stable team identities, full names, validated codes, the copied
  per-match configuration, and current display order. Do not read new values
  from a subsequently edited saved preset or reverse the display swap.
- Start set 1 with points and sets won at 0–0, no earlier-set baseline or recorded
  results, full configured timeout allowances, no selected server, no pending
  result/cap conflict, and no current-set continuation approval. A reset does not
  grant permission for a later set to exceed its cap.
- Remove the old match's Undo opportunity. Q26 already excludes Undo across a
  confirmed reset. Later accepted match edits create their own one-step Undo.
- Keep the current stream, destination/account, camera, mute, and controller
  authorization independent. Reset does not start, stop, reconnect, or reconfigure
  the broadcast. Existing controllers need fresh match state before further edits.
- The watch can display the result but cannot initiate, confirm, or bypass the
  confirmed reset restriction. Ordinary serve/scoring permissions remain intact.

The technical design must prevent delayed commands or duplicate reset delivery
from clearing/changing the reset match again. A new match identity or epoch and
revision-bound confirmation are existing proposals, not selected mechanisms.
Do not erase duplicate protection merely because match history and Undo are
cleared. Receipt lifetimes, storage, session expiry, and pruning remain D04/D11
technical work. This reset creates no cloud archive or match-history browser.

## Written review cases

S01–S14 apply Q33–Q35 alongside Q26/Q27/Q30. S12/S13 retain technical freshness
and duplicate-handling proposals whose mechanisms still need a disposition.
These are written examples, not executable tests or evidence of delivered behavior.

| ID | Starting state | Action | Expected result under the confirmed policies / noted technical proposals |
| --- | --- | --- | --- |
| S01 | Best-of-5 set 1 at 26–24, serve A, totals 0–0, timeout allowance 3 and remaining 1–0 | Confirm the nonfinal set | Record 26–24, derive totals 1–0, and start set 2 at 0–0 with timeouts 3–3 and no selected server. Do not copy A as the new server or change display order. |
| S02 | S01's new set with no server | Award B an ordinary rally | Reject or require explicit server selection first; do not change points, serve, timeouts, history, or Undo. Never infer service from the attempted point. |
| S03 | S01's new set, swapped display order B-left/A-right, authorized synchronized watch | Explicitly select B as first server, then award A a rally | Selection shows B as server without changing points. The accepted A rally gives A one point and serve; ownership follows team identity despite display order. |
| S04 | First-server selection in S03 is the latest edit, before any rally or other accepted match edit | Undo that selection from any role permitted by Q27, then request another Undo | Restore no server and keep ordinary rally input blocked. Q26 consumes Undo; a repeat cannot undo the earlier set confirmation. |
| S05 | Ongoing mid-match setup at 7–5 with explicitly supplied serve B; later stored durably | Restore the match or replace matching early baseline history | Preserve serve B and current play; do not treat the ongoing set as a freshly started set. Atomic persistence remains unverified. |
| S06 | Best-of-3 deciding set at 16–14 and potential match winner A | Confirm the match | Retain the completed match and final points under the existing PRD. Create no set 4 or first-server prompt. Streaming stays independent. |
| S07 | A valid completed-history correction leaves totals 1–1 in best-of-3 | Commit the correction under Q30, then try a rally before server selection | Q30 reopens deciding set 3 at 0–0 with full timeouts and no server. Require explicit selection before the rally; Q33's ordinary-set policy is consistent with this existing rule. |
| S08 | Match with names/codes, custom settings, baseline/history, active cap continuation, swapped display order, and an Undo opportunity | Open reset review and cancel | Preserve every committed match value and the prior Undo opportunity. Preserve current media/account/controller state. |
| S09 | S08, custom timeout allowance 3, original copied settings differ from a saved preset edited later | Confirm reset on an authorized phone/tablet | Keep teams/names/codes, copied match settings, and display order; start set 1, points/sets 0–0, timeouts 3–3, no baseline/history/Undo/server/continuation or pending result. Keep saved preset and broadcasting unchanged. |
| S10 | S09 has reset the match | Request Undo; then select a server and award a point | Existing Q26 prevents Undo of reset. The new accepted serve/point edits have their own latest-edit Undo; no older-match Undo is recovered. |
| S11 | S08; authorized second Android phone/tablet or watch is connected | Separately request reset from each role | Under Q35, the Android controller may review/confirm reset; the watch is denied without mutation and can view the fresh result. Existing serve/Undo permission cannot bypass this restriction. |
| S12 | Reset was reviewed at r40; a timeout edit commits at r41 | Submit the old reset confirmation | Proposed freshness rule: reject the stale review and preserve the timeout/current match. Refresh and review the current effects before a new confirmation. Exact revision mechanism remains open. |
| S13 | Reset accepted once; response lost; operator later selects B and records a point in the fresh match | Retry that old reset, or deliver an old-match point | Do not reset again or apply an old point to the fresh match. Preserve the post-reset edits; eventual receipt/identity/freshness rules must establish this behavior. |
| S14 | Confirmed reset at set 1 with no server, continuation, or Undo; app later restarts | Restore durable match state | Preserve the reset state, require first-server selection, and do not recreate old history/Undo/continuation. Existing restart policy does not silently start broadcasting. Storage and device behavior remain untested. |

## Handoff and remaining work

- [M3.2](../subplans/m3-2-rally-points-and-serve.md) consumes first-server selection,
  the unset-server rally block, and serve-edit Undo consequences.
- [M3.4](../subplans/m3-4-set-and-match-confirmation.md) consumes ordinary next-set
  initialization while retaining final points and Q30's reopening rule.
- [M3.5](../subplans/m3-5-undo-and-confirmed-reset.md) consumes reset contents,
  explicit confirmation, selected authority, and the established no-Undo boundary.
- [M6.2](../subplans/m6-2-ordered-idempotent-commands.md),
  [M3.7](../subplans/m3-7-atomic-persistence-and-restart.md), and
  [M7.2](../subplans/m7-2-watch-match-controls.md) consume current-state checks,
  atomic state/Undo/receipt handling, and the selected role permissions.

Q33–Q35 settle all three owner choices in this review. Later Q38 separately
requires confirmed reset to preserve the streaming phone's placement preference;
see the [placement/control review](d10-placement-control-policy-review.md).
The later
[cap-permissions review](d11-cap-continuation-permissions-review.md) records Q36's
phone/tablet-only continuation confirmation and latest-confirmation Undo.
Technical reconciliation, identity, receipt/expiry/storage mechanisms, and other D11 technical
dispositions remain separate. G1/G2 and public-boundary confirmation still apply.
M1.5 remains complete for documentation; task counts and gate states do not change.

## Skill use and evidence limits

Read the installed find-skills, PRD, volleyball-rules, and plan-status guidance.
Checked installed directories and focused local matches for first-server and
reset guidance. This continues the existing M1.5 area; the available project
skills cover the review. No fresh online skill search, upstream integrity check,
or new installation is claimed. These are product policies, without a
new platform or official-rule claim. No application code, dependency, prototype,
executable application test, device test, or provider action was added.

## Pre-answer documentation validation

Validated on 2026-10-21 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 74 Markdown documents,
1,175 local links, 271 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills.

Separate reconciliation confirmed unique sequential S01–S14 and Q1–Q32,
three pending answers, unchanged task counts of 7 complete / 1 in progress /
45 not started, and 13 open aggregate decision records. Completed task acceptance
lists remain checked; not-started task lists remain unchecked. AGENTS.md and
skills-lock.json retain their pre-work SHA-256 hashes.

Manual review checked custom timeout allowances, stable team ownership, unset
serve and Undo, restored current play, final-point retention, Q30 reopening,
reset cancellation, proposed reset permissions, stale/duplicate requests, and
independence from broadcasting. These checks establish documentation consistency,
without application behavior, physical-device support, or provider evidence.

## Post-answer synchronization and validation

The owner selected all three recommended answers on 2026-10-21. Q33–Q35 now
confirm explicit first-server selection before ordinary rally input, reset
retaining teams/settings/display order while clearing progress, and confirmed
reset on phones/tablets only. The PRD acceptance criteria/control matrix,
M1.5 register/examples, affected task handoffs, companion reviews, architecture,
master plan, index, and status dashboard are synchronized. Cap-continuation
controller allocation and technical mechanisms remain open.

The documentation checker passed after synchronization: 74 Markdown documents,
1,180 local links, 274 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills; exit code 0, no errors.
Separate reconciliation confirmed unique sequential Q1–Q35 and S01–S14,
three confirmed answer rows, seventeen confirmed D11 owner choices, unchanged
task counts of 7 complete / 1 in progress / 45 not started, and 13 open aggregate
decision records. All not-started acceptance boxes remain unchecked. AGENTS.md
and skills-lock.json retain their pre-work SHA-256 hashes.

Manual review distinguished confirmed product policies from proposed identity,
revision, freshness, and receipt mechanisms. No application code, dependencies,
tests, physical-device results, or provider actions were added; G1/G2 and public
test-boundary requirements remain unchanged.

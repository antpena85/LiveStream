# M1.5 match corrections, caps, and undo

Recorded: 2026-10-18 (America/New_York)  
Policy follow-up reviewed: 2026-10-20; four additional owner answers recorded  
Undo/watch follow-up answered: 2026-10-21; two additional owner decisions recorded  
Completed-match entry follow-up answered: 2026-10-21; one additional owner decision recorded  
Cap-input follow-up answered: 2026-10-21; one additional owner decision recorded  
Historical-correction/reopening follow-up answered: 2026-10-21; one additional owner decision recorded  
Baseline/history follow-up answered: 2026-10-21; two additional owner decisions recorded  
New-set serve/reset follow-up answered: 2026-10-21; three additional owner decisions recorded  
Cap-continuation permissions follow-up answered: 2026-10-21; one additional owner decision recorded  
Status: Planning specification; eighteen owner decisions recorded, remaining D11 technical mechanisms open.  
Task: [M1.5](../subplans/m1-5-match-correction-specification.md)  
Authority: [FR-05 and scoring presets](../../PRD.md#fr-05-enter-and-correct-match-information)  
Decision: [D11](../MASTER_PLAN.md#7-open-decision-register)  
Design companion: [Match invariants](../../ARCHITECTURE.md#5-match-model-and-scoring-invariants)

## Authority and evidence

This is the requested planning deliverable. The examples specify observable
outcomes for review; no application behavior or executable test has been built.
An example marked **Proposed** is conditional on its associated D11 choice.
Writing it does not approve that policy or confirm a public test boundary.

The [2026-10-21 Undo/watch review](d11-undo-and-watch-policy-review.md)
records Q26/Q27: all listed accepted match edits share one-step Undo, which is
consumed on use, and the watch may undo a latest cross-device edit only when
that action is in its allowed controls. Its twelve written cases, U01–U12,
apply those confirmed policies.

The [completed-match entry review](d11-completed-start-policy-review.md)
records Q28: an already-finished match entered at setup needs a complete ordered,
valid set history. It distinguishes that case from Q23's ongoing totals-only
baseline. Its C01–C07 cases apply the confirmed policy.

The [cap-input review](d11-cap-input-policy-review.md) records Q29: a first
cap-reaching one-point lead enters paused conflict, while tied-at-cap or
above-cap starting/restored input needs explicit acknowledgement of prior
current-set continuation. Its K01–K09 cases apply the confirmed policy.

The [historical-correction review](d11-history-reopen-policy-review.md) records
the confirmed D11.5/Q30 policy. Its H01–H06 cases reopen a completed match only
after a coherent phone/tablet correction leaves neither team with enough wins.

The [baseline/history review](d11-baseline-history-policy-review.md) records
Q31/Q32 and B01–B12 for replacing unknown early history
without double counting and correcting a starting baseline after later play.
Both policies are confirmed; revision, storage, and atomic-commit mechanics remain open.

The [new-set serve/reset review](d11-new-set-serve-and-reset-policy-review.md)
records Q33–Q35 and S01–S14: explicit first-server selection before ordinary
rally input, reset retaining teams/settings/display order, and reset authority
on phones/tablets only. Technical identity/revision/storage mechanisms remain open.

The [cap-continuation permissions review](d11-cap-continuation-permissions-review.md)
records Q36 and CP01–CP10: the streaming phone and authorized second Android
phone/tablet may confirm live continuation and Undo its latest confirmation.
The watch shows the conflict and directs these actions to a phone; it never
skips the restricted edit. Setup/history/reset restrictions remain separate.

The D10 [placement/control review](d10-placement-control-policy-review.md)
subsequently records Q40 and PU01–PU08: placement is outside match Undo.
Changing position preserves the existing opportunity; Undo of the latest match
edit keeps the current position. This does not alter Q27's watch authority,
rearm consumed Undo, or count placement as an additional D11 match-edit policy.

The later D10 [numeric-fit review](d10-numeric-fit-and-overflow-policy-review.md)
records Q44/Q45 on 2026-10-23: adapt the readable layout; if valid numbers still
cannot fit, preserve the full new match and stop publishing with notice while
scoring continues off-air. This is the diagnosed numeric-fit exception to
ordinary reset/Undo/media independence. An accepted numeric Undo still consumes
its opportunity; stream stop/restart is not itself undone. These are D10 owner
policies, not additional D11 choices or selected storage/shutdown mechanisms.

The approved PRD establishes customized indoor presets, nonnegative counts,
explicit set/match confirmation, undo, confirmed reset, historical corrections,
consistent set totals, stable team identity, and retained match information.
The [volleyball-rules skill](../../.agents/skills/volleyball-rules/SKILL.md)
provides supporting guidance. The PRD takes precedence: use the match's custom
timeout allowance, retain final-set points, and preserve the phone/watch control
matrix. A display swap is deliberate; no automatic swap or reminder is added.

Official reference checked on 2026-10-18: FIVB rules 6.1.3, 6.2, 6.3, and 15.1
in the [2025–2028 indoor rules](https://www.fivb.com/wp-content/uploads/2025/01/FIVB-Volleyball_Rules2025_2028-EN-v05.pdf).
They support sideout serving, the standard 25/15 targets with a two-point lead,
three sets to win, and two team timeouts per set. Best-of-3 is the PRD's variant.
Custom caps and application undo/correction policies are governed by the PRD
and D11 owner decisions; the FIVB reference does not decide them.

## State and notation used in examples

- Scores, sets, and remaining timeouts are always written **A–B**, using stable
  team identities. Display positions never change this notation.
- Unless a row overrides it, the configuration is best-of-5: three sets to win,
  regular target 25, deciding target 15, margin 2, no cap, two timeouts per team.
- A revision identifies a committed match state. Illustrative revisions such as
  `r10` are example labels, not a selected storage schema.
- `Playing`, `Potential winner`, and `Complete` describe match status separately
  from streaming status. Reaching a potential winner never confirms a set.
- In an accepted operation, unspecified match fields remain unchanged. A rejected
  or canceled operation changes no committed match values or match revision.
  Recording rejection/receipt metadata is a separate implementation concern.
- Proposed commands identify the match, target team/set, and expected revision.
  Accepted changes produce a newer revision; Undo does not rewind it.

## Owner decisions recorded on 2026-10-18

The owner selected these answers during M1.5:

1. **Point removal leaves serve unchanged.** Removing a point is a correction;
   Undo restores the recorded previous score and server.
2. **Reject conflicting historical corrections.** Keep recorded play intact and
   require a coherent reviewed correction if later sets would become inconsistent.
3. **Latest match edit only.** Retain one-step Undo across app restart; use manual
   correction for earlier mistakes.

These answers are reflected in [PRD Q16–Q18](../../PRD.md#53-confirmed-scope-decisions).
They confirm only those choices, not the remaining scope details or technical
proposals below. D11 remains open as an aggregate decision record.

## Owner decisions recorded on 2026-10-20

The owner answered the focused follow-up questions:

1. **Preserve the winning margin when a cap conflicts.** With target 25,
   margin 2, and cap 30, a 30–29 result cannot be confirmed as a set win.
   Explicit conflict resolution is required and selected in item 4 below.
   Q29 later settles initial/restored tied-at-cap/above-cap input handling.
   The prior cap-overrides-margin
   proposal was not selected. Customizable margins and the uncapped defaults
   remain in scope.
2. **Allow totals-only starting sets.** Keep known starting totals as a
   baseline and record later sets without inventing earlier scores or win order.
   This does not select already-completed starting-match entry or storage details.
3. **Pause rally scoring at a potential winner.** Keep corrections available;
   require confirmation or correction before another ordinary rally.
4. **Confirm current-set continuation at a cap conflict.** The operator must
   confirm "continue win by two" before scoring beyond the cap. Preserve the
   configured winning margin; the exception applies only to the current set.
   Canceling does not authorize continuation, and later sets retain their cap.

[PRD Q22–Q25](../../PRD.md#53-confirmed-scope-decisions) retain these answers.
They do not approve the later Undo/watch, correction-interaction, serve, reset,
or retention proposals. The separate cap-conflict resolution question has been
answered; no general mid-match configuration editor was selected.
The new continuation confirmation is not silently added to every controller's
capabilities. Subsequent Q26/Q36 confirm Undo coverage and phone/tablet-only live
continuation/Undo authority; those later answers update the PRD control matrix.

## Owner decisions recorded on 2026-10-21

The owner selected both recommendations from the focused Undo/watch review:

1. **All accepted match edits share one-step Undo.** Include points, serve,
   timeouts, names/codes, display swaps, starting-value corrections, set/match
   confirmation, valid historical corrections, and current-set cap-continuation
   confirmation. Exclude stream/account actions and confirmed reset. A successful
   Undo consumes the opportunity; it cannot become Redo or reach an older edit
   until a new accepted match edit occurs.
2. **Watch Undo follows its action permissions.** It may undo the latest point,
   serve, timeout, display-swap, or set-confirmation edit regardless of whether
   a phone or watch made it. For names/history or another restricted latest edit,
   it directs the operator to a phone and never skips that edit.

[PRD Q26/Q27](../../PRD.md#53-confirmed-scope-decisions) retain these answers.
They do not allocate current-set cap-continuation to the watch, change its
view-only fields, settle reset authority, or select storage/receipt mechanics.

## Baseline/history owner decisions recorded on 2026-10-21

The owner selected both recommendations in the baseline/history review:

1. **Replace a baseline with matching complete early history.** Validate the
   supplied ordered prefix and replace its baseline contribution; count every
   set once and preserve later recorded play. Do not invent unknown results.
2. **Permit valid reviewed baseline corrections after later play.** Review on
   a phone/tablet and recompute affected totals and set state together. Reject
   any correction that invalidates or discards later play, leaving the committed
   match intact.

[PRD Q31/Q32](../../PRD.md#53-confirmed-scope-decisions) retain the answers.
The B01–B12 cases apply them, including changed set numbers/targets, Q30
reopening, and Q26/Q27 Undo limits. No persistence or protocol mechanism is selected.

## New-set serve/reset owner decisions recorded on 2026-10-21

The owner selected all three recommendations in the serve/reset review:

1. **Choose a server explicitly for each new set.** Leave serve unset and block
   ordinary rally input until an authorized controller selects a team. Preserve
   an explicitly supplied or durably recorded server for an ongoing set.
2. **Retain teams, settings, and display order on reset.** Clear scores,
   sets-won baseline/totals, history, Undo, and cap-continuation approval; start
   set 1 at 0–0 with full configured timeouts and no selected server.
3. **Permit reset on phones/tablets only.** Require deliberate confirmation on
   the streaming phone or authorized second Android phone/tablet; deny watch reset.

[PRD Q33–Q35](../../PRD.md#53-confirmed-scope-decisions) retain the answers.
S01–S14 apply the policies and existing Undo/reopening requirements while keeping
technical identity, freshness, storage, and receipt mechanisms unresolved.

## Cap-continuation permission recorded on 2026-10-21

The owner selected phone/tablet-only live continuation confirmation. The watch
shows the paused conflict and directs the operator to a phone; it cannot confirm
continuation or Undo that latest confirmation, and must never skip it to reach
an older edit. Permitted phones/tablets retain Q26's latest-edit Undo. A later
allowed point/serve/timeout edit may still be undone from the watch under Q27.

[PRD Q36](../../PRD.md#53-confirmed-scope-decisions) retains the answer.
CP01–CP10 apply that allocation without changing Q29 setup acknowledgement or
selecting identity, revision, receipt, expiry, or storage mechanisms.

## D11 policy register

Rows distinguish confirmed portions from unresolved choices. Recommendations
in an open portion are not selected defaults.

| ID | Recommended proposal and alternative | Decision or evidence still needed |
| --- | --- | --- |
| D11.1 — Point subtraction | Remove the selected team's point and leave serve unchanged; Undo restores recorded prior state. | Confirmed by the owner, 2026-10-18; PRD Q16. |
| D11.2 — Hard cap | Confirmed: preserve the configured margin; a one-point lead at the cap is not a winner. Require explicit current-set continuation before scoring beyond the cap; later sets retain their cap. A cap must be at least its applicable target. At setup/restore, a first cap-reaching one-point lead enters paused conflict; tied-at-cap or above-cap input needs explicit prior-continuation acknowledgement. Q36 allocates live confirmation and Undo of its latest confirmation to permitted phones/tablets; the watch directs those actions to a phone and never skips the edit. | Q22/Q25/Q29/Q36 confirm scoring/input and controller policy. The [permissions review](d11-cap-continuation-permissions-review.md) supplies CP01–CP10. Persistence, identity/revision, and technical retention remain open. |
| D11.3 — Starting values | Confirmed: permit starting totals as a baseline when old scores are unknown; never invent earlier scores/order. Completed entry at setup needs complete ordered valid history, derived totals, retained final points, and no active next set/server/timeouts. Under Q31, complete matching early history replaces the baseline without double counting. Under Q32, a reviewed phone/tablet correction to starting totals after later play is allowed only when the resulting sequence remains valid; never invalidate or discard later play. | Baseline entry Q23, completed entry Q28, and reconciliation policy Q31/Q32 are confirmed. The [baseline/history review](d11-baseline-history-policy-review.md) supplies B01–B12. Revision, storage, and atomic validation/commit mechanisms remain open. |
| D11.4 — Undo scope and depth | Confirmed: latest accepted match edit only, retained across restart. Include points, serve, timeouts, names/codes, display swaps, starting-value corrections, set/match confirmation, valid historical corrections, and current-set cap-continuation confirmation. Exclude stream/account actions and confirmed reset. A successful Undo consumes the opportunity; a second Undo changes nothing until a new accepted match edit. The watch may undo the latest phone/watch point, serve, timeout, display-swap, or set-confirmation edit; restricted latest edits require a phone and are never skipped. | Depth/restart confirmed under Q18; scope/repeat/watch authority confirmed on 2026-10-21 under Q26/Q27. Atomic persistence, authorization freshness, and receipt/retention mechanics remain open. |
| D11.5 — Historical correction and reopening | Reject a correction that makes later play inconsistent; keep recorded play intact and require a coherent reviewed correction. Review a valid correction on a phone/tablet and reopen the required next set only when its corrected completed history leaves neither team with enough wins. | Rejection policy Q17 and review/reopening Q30 are confirmed. Q31/Q32 settle baseline/history policy. Revision-bound atomic reconciliation remains technical work. |
| D11.6 — Confirmation and identity | Confirmed: pause ordinary rally input at a potential winner under Q24; require first-server selection for every new set under Q33. Q34 retains teams/names/codes/copied settings/display order on reset, clears scoring/baseline/history/Undo/continuation, and starts set 1 at 0–0 with full timeouts and no server. Q35 permits confirmed reset on phones/tablets only; Q26 excludes reset from Undo. Proposed: bind reviewed actions to revisions and use a fresh match identity/epoch across reset. | Q33–Q35 and S01–S14 are retained in the [new-set serve/reset review](d11-new-set-serve-and-reset-policy-review.md). Revision/identity/storage mechanisms remain open. |
| D11.7 — Receipt lifetime and expiry | Retain accepted-command receipts for every interval in which a retry could be accepted. Reject obsolete commands after pruning; resynchronize after reconnect/restart. | Technical lifetime, expiry/clock model, pruning, and storage bounds. No numeric value is selected here. |

The owner questions provided clear alternatives and a custom-answer
field; the selected answers above are evidence. Other choices are recorded for later
focused decisions. M1.5's documentation completion cannot close the entire D11
record or approve the proposed implementation mechanics.

## Points, serve, timeouts, and display identity

| Example / basis | Starting state | Command | Expected result |
| --- | --- | --- | --- |
| E01 — PRD sideout | Set 1, points 10–9, serve A, timeouts 1–2 | Award a rally to B | Points 10–10, serve B, timeouts 1–2. |
| E02 — PRD serving rally | Set 1, points 10–9, serve A, timeouts 1–2 | Award a rally to A | Points 11–9, serve A, timeouts 1–2. |
| E03 — Confirmed D11.1 | State after E01: points 10–10, serve B, timeouts 1–2 | Remove one B point | Points 10–9; serve stays B. This does not infer the previous server. |
| E04 — Confirmed D11.4 | E01 was the latest match mutation at r11; its before-state was r10 | Undo that mutation against r11 | Points 10–9, serve A, timeouts 1–2 at r12. Stream state is unchanged. |
| E05 — PRD nonnegative points | Points 0–9, serve B | Remove one A point | Reject; points remain 0–9 and serve B. |
| E06 — PRD custom allowance | Custom allowance 3, remaining timeouts 3–1, points 8–6, serve A | Record one A timeout | Remaining timeouts 2–1; points and serve stay 8–6 and A. |
| E07 — PRD bounds | Allowance 3; remaining timeouts 0–1 | Record another A timeout, or correct B's remaining count to 4 | Reject either command separately; remaining counts stay 0–1. |
| E08 — Confirmed D11.4 | E06 was the latest mutation, with before-counts 3–1 | Undo E06 against its current revision | Remaining timeouts return to 3–1; points 8–6 and serve A are preserved. |
| E09 — PRD identity | Points 10–7, sets 1–0, timeouts 1–2, serve A, display A-left/B-right | Swap display sides | Display B-left/A-right; every value and serve remain attached to its original team. |
| E10 — Proposed revision checks | A controller saw A-left at r20; a swap committed at r21 | Submit an A point with expected revision r20 | Reject as stale and return fresh state. Do not reinterpret the tap as a point for the new left team B. |

## Targets, caps, and explicit confirmations

The leading team is a potential winner only when its score reaches the
applicable target and its lead reaches the configured margin. Under confirmed
Q22, a cap does not create a second route that bypasses that margin. A cap/margin
conflict requires the explicit current-set continuation selected under Q25.
A score such as 30–29 at cap 30/margin 2 is not eligible for set confirmation.
No automatic continuation beyond the cap or margin waiver is approved. After
the operator confirms "continue win by two", allow scoring beyond that set's
cap while preserving the margin; do not apply the exception to later sets.
Pause ordinary rally input at the conflict until that confirmation or a
correction. Cancellation leaves the conflict unresolved, keeps ordinary rally
input paused, and preserves committed match values.
Potential winners still require confirmation before advancing or ending a match.

Proposed configuration checks for D11.2: targets, winning margin, and sets
needed to win are positive integers; timeout allowance is a nonnegative integer;
an optional cap is an integer at least as large as each applicable target.
Scores and counts are integers. Exact implementation limits remain TBD.
The no-cap default stays available; no cap is inferred from a high score.

| Example / basis | Starting state | Command | Expected result |
| --- | --- | --- | --- |
| E11 — PRD deuce | Set 1, points 24–24, no cap | Award A a rally | 25–24, serve A; no potential winner yet. |
| E12 — PRD confirmation | Set 1, points 25–24, sets 0–0, no cap | Award A a rally | 26–24, serve A, potential winner A; still set 1, sets 0–0, with no automatic timeout reset. |
| E13 — PRD next set / Q33 | E12 at the current revision; custom timeout allowance 3, remaining 1–0 | Confirm set 1 for A | Record 26–24; sets 1–0, set 2 points 0–0, timeouts 3–3, and no selected server. Require explicit selection before ordinary rally scoring. |
| E14 — PRD best-of-3 | Best-of-3, sets 1–1, deciding set 3 points 14–14 | Award A a rally, then another A rally | First result 15–14 is not a winner; second result 16–14 is a potential match winner. No automatic completion. |
| E15 — PRD final retention | State after E14, current timeouts 1–0 | Confirm the deciding set and match for A | Complete, sets 2–1, final points 16–14 and timeouts 1–0 retained. No set 4 or empty replacement score. |
| E16 — Confirmed D11.2 margin precedence | Target 25, margin 2, cap 30, points 29–29 | Award B a rally | 29–30, serve B, no potential winner. Require Q25 continuation confirmation before scoring beyond the cap; do not confirm a one-point win or infer an automatic cap override. |
| E17 — Confirmed D11.2 margin satisfied | Target 25, margin 2, cap 30, points 24–23 | Award A a rally | 25–23 is a potential winner; pause ordinary rally scoring until confirmation or correction under Q24. The cap is not a required target. |
| E18 — Confirmed D11.2/Q29 cap input | Initial/restored setup or correction draft, target 25 and cap 30, with no known continuation confirmation | Enter 30–30 or 31–29; separately try cap 24 | Reject tied-at-cap or above-cap input unless the operator explicitly acknowledges prior current-set continuation; do not infer an earlier override. Reject cap 24 because it is below target. A 30–29 input enters paused CapConflict under Q29. |
| E19 — Proposed D11.6 stale confirmation | Set 1 was 25–23 at r30; a correction makes it 24–23 at r31 | Confirm the old 25–23 result against r30 | Reject; stay at 24–23 without changing history or set totals. |

Associated behavior and remaining choices:

- **Further rally input at a potential winner:** confirmed on 2026-10-20 under
  Q24: pause ordinary rally scoring until confirmation or correction; keep
  score corrections available. This does not turn a cap/margin conflict into
  a potential winner. Q25 separately requires explicit current-set continuation.
- **Serve for a new set:** confirmed under Q33: leave serve unset and require
  explicit team selection before ordinary rally input. Do not carry forward
  the last rally's server or infer an alternating order. Exact UI remains
  proposed; no player rotation or coin-toss workflow is added.

Q34/Q35 confirm reset contents and phone/tablet authority. A fresh match
identity/epoch and revision-bound confirmation remain proposed mechanisms.
The [focused serve/reset review](d11-new-set-serve-and-reset-policy-review.md)
records Q33–Q35, including the unset-server interaction, watch restrictions,
reset cancellation, Undo, and delayed-command review cases.

## Starting values and completed-set corrections

Confirmed D11.3/Q23 entry permits totals-only baselines. Q31/Q32 reconciliation:
starting baseline plus wins in separately recorded
confirmed sets equals displayed sets won. Do not count the same set in both.
For a new ongoing set, its number is one plus the number of baseline and recorded
completed sets; in a completed match the last set stays visible. No sequence may
contain play after either team has already won the required number of sets.

When earlier scores are unknown, a baseline represents only known set totals.
It supplies no invented point scores, order of those earlier wins, or server.
If earlier win order becomes necessary to validate a correction, request the
missing history instead of guessing. For fully supplied history, validate set
order, each applicable regular/deciding target, and the earliest match finish.
Changing a baseline or a completed result must recompute affected totals and
phase together. A totals-only edit cannot contradict known results.

| Example / basis | Starting state | Command | Expected result |
| --- | --- | --- | --- |
| E20 — Confirmed D11.3/Q23/Q31/Q32 accounting | New best-of-5 setup; older scores unknown | Enter starting sets 1–0, current points 7–5, serve B, remaining timeouts 1–2 | Accept the explicit baseline without fabricated earlier scores (Q23). Create ongoing set 2; later confirmation of A's 25–20 records only set 2, derives total 2–0, and starts set 3. Count each completed set once. |
| E21 — Confirmed D11.3/Q31 history consistency | New setup with set 1 recorded as A 25–20 | Also enter displayed starting totals 0–1 | Reject the conflicting draft; do not silently change history or double-count baseline totals. |
| E22 — Confirmed D11.3/Q28 validation | New ongoing best-of-5 setup | Enter starting totals 3–0 with an active next set | Reject this ongoing-match draft: the required wins were already reached. Q28 permits a completed entry only with a complete ordered valid history, not totals alone. |
| E23 — PRD correction; proposed reconciliation | Recorded set 1 A 25–20, set 2 B 25–22; active set 3 points 8–6, serve A, timeouts 1–2 | Correct set 1 to A 25–21 | Retain totals 1–1, current set, serve, and timeouts; history reflects 25–21. Do not reset current play. |
| E24 — Proposed D11.5 valid winner change | Same starting history/current play as E23 | Correct set 1 to B 25–23 | Recompute totals to 0–2; active regular set 3 at 8–6 remains valid and is preserved. |
| E25 — Confirmed D11.5 inconsistent later play | Sets 1 and 2 won by A; set 3 won by B 25–20; active set 4 points 8–6 | Change set 3 to an A 25–20 win | Reject the whole correction, because A would have won 3–0 before set 4. Preserve all prior history, totals 2–1, and set 4. Explain the conflict; do not delete set 4. |
| E26 — Confirmed D11.5 invalid deciding set | Best-of-3 completed 2–1: A won set 1, B set 2, A set 3 by 15–13 | Change set 1 to a B win | Reject: B would have finished 0–2 before set 3. Do not keep an impossible deciding set or silently discard it. |
| E27 — PRD post-final correction | Best-of-3 completed 2–0; A won set 2 by 25–20 | Correct set 2 to A 25–21 | Remain Complete at 2–0; update the visible final score to 25–21 without creating a new set. |
| E28 — Proposed D11.5 conditional reopen | Same initial complete match as E27 | Correct set 2 to B 25–23 | The coherent history now gives 1–1: reopen for deciding set 3 with points 0–0 and configured full timeouts. Preserve both completed scores. Reopening does not start a stream. |

Q31 replaces a baseline with complete matching early history and counts its sets
once. Q32 permits reviewed starting-total corrections after later play only if
the entire resulting sequence remains valid. B01–B12 cover preserved current
play, changed set numbers/targets, rejection, and Q30 reopening.

For the confirmed rejection policy, the proposed conflict result identifies the
first inconsistent set and leaves committed data intact. A reviewed coherent
correction can change the affected history/current state together only after
validation and confirmation against the reviewed revision. Q30–Q32 settle the
phone/tablet review and baseline/history policies; revision and atomic-commit
mechanisms remain technical work. No deletion of later play is authorized.
Recomputing does not fabricate an unknown first server or change display order.

## Undo, reset, and command retention

Confirmed D11.4 scope: one-step Undo of the latest accepted match edit, retained
across restart; earlier mistakes use manual correction. It includes the Q26
action list, excludes stream/account actions and confirmed reset, and is consumed
on success until a new accepted match edit. The streaming phone's accepted order
applies regardless of which scoring device submitted the edit. Proposed mechanics
show the affected action and require its current revision. Save
the before-state with the mutation so Undo can restore points, serve, timeouts,
history, names, display order, and phase where the action changed them.
Watch Undo remains required by the PRD. Under Q27, it can reverse the latest
phone/watch edit only when that action is within its allowed controls: point,
serve, timeout, display swap, or set confirmation. A names/history or another
restricted latest edit directs the operator to a phone and blocks a skipped Undo.
Do not infer permission for direct name/history editing from the watch's Undo.

Under the confirmed policy, Undo commits a fresh revision and consumes that opportunity;
there is no automatic Redo/toggle, repeated Undo, or Undo of confirmed reset.
A new accepted match mutation creates a new opportunity. Rejected commands,
connection/status updates, and camera/mute/stream/account actions do not consume
it. Atomic representation, capability enforcement, and receipt retention remain
open; one-step depth, restart availability, scope, repeat behavior, and watch
authority are confirmed.

| Example / basis | Starting state | Command | Expected result |
| --- | --- | --- | --- |
| E29 — Confirmed D11.4 scope; proposed freshness | Controller reviewed Undo of A's point at r40; B's timeout commits at r41 | Submit the reviewed Undo against r40 | Reject; do not undo B's timeout or the older point unexpectedly. Show current state and the actual latest undoable action. |
| E30 — Confirmed D11.4 restart | Last mutation was E01 at r11 and its before-state is durably recorded; app restarts | After refresh, Undo E01 | Restore E04's match values at a newer revision. Do not resume broadcasting. No hardware or persistence result is claimed by this example. |
| E31 — Confirmed D11.4 scope; proposed freshness | E13 is the latest mutation; set 2 is still untouched at 0–0 | Undo E13 | Remove that recorded confirmation, restore set 1 at 26–24 and timeouts 1–0, totals 0–0, Potential winner. If a new rally already committed, this old Undo is stale. |
| E32 — PRD reset confirmation | Match X, set 2 points 8–6, stream active | Open reset and cancel it | Retain match X and all its data; broadcasting is unaffected. |
| E33 — Confirmed Q34/Q35 contents; proposed reset identity | Match X at its reviewed revision, selected configuration retained | Confirm reset from an authorized phone/tablet | Retain teams/names/codes, copied settings, and display order. Start set 1 at points/sets 0–0 with full configured timeouts, no baseline/history/Undo/continuation, and no selected server. Keep stream/account state separate. Fresh identity/epoch Y is a proposed mechanism; watch reset is denied. |
| E34 — Proposed D11.6 late old command | Reset E33 committed as Y | Deliver an old score command targeting X | Reject for old match identity; Y is unchanged even if revision numbers happen to coincide. |
| E35 — PRD duplicate safety; proposed receipt handling | Command C added B's point once, r50 became r51; acknowledgment was lost | Retry identical C, including after app restart | Return its original accepted outcome and fresh current state without adding a second point. Check its receipt before rejecting it merely for the old expected revision. |
| E36 — Proposed D11.7 expiry | A command's acceptance window/session is closed; its receipt has been pruned under the eventual policy | Deliver that obsolete command | Reject as obsolete; never treat the absent receipt as permission to apply it anew. |

Undo retention and duplicate-receipt retention solve different problems.
Removing an Undo opportunity must not remove the protection against a duplicate
score command. A reset changes match identity; it need not stop the media session.
Connection generations and expiry prevent old controller work from being accepted
after reconnect/restart. Numeric windows, clock handling, and safe pruning await
M2.5/M2.7 and D04/D11; no arbitrary timeout is chosen in this specification.

## Delivery handoff and acceptance review

| Consumer | Review material and remaining entry conditions |
| --- | --- |
| [M3.1](../subplans/m3-1-match-setup-and-presets.md) | E18, E20–E22, C01–C07, K01–K09, and B01–B12; apply confirmed margin/cap-input/baseline/completed-entry and Q31/Q32 reconciliation policies, then validate copied configurations. |
| [M3.2](../subplans/m3-2-rally-points-and-serve.md) / [M3.3](../subplans/m3-3-timeout-counts-and-corrections.md) | E01–E08; apply confirmed point subtraction and review custom count validation. S01–S07 apply Q33 first-server selection and rally blocking. |
| [M3.4](../subplans/m3-4-set-and-match-confirmation.md) | E11–E19, K01–K09, S01–S07, and CP01–CP10; apply confirmed cap/input/first-server rules and Q36 phone/tablet continuation authority; resolve revision-bound confirmation mechanics. |
| [M3.5](../subplans/m3-5-undo-and-confirmed-reset.md) | E04, E08, E29–E34, U01–U12, S08–S14, and CP04/CP06/CP07; apply confirmed Undo/reset rules and Q36's watch restriction; resolve identity/revision/storage mechanisms. |
| [M3.6](../subplans/m3-6-historical-set-corrections.md) | E23–E28, H01–H06, and B01–B12; apply Q30 conditional reopening and Q31/Q32 baseline/history reconciliation. Revision and atomic-commit mechanisms remain open. |
| [M3.7](../subplans/m3-7-atomic-persistence-and-restart.md) / [M6.2](../subplans/m6-2-ordered-idempotent-commands.md) | E10, E29–E36; atomic state/receipt/undo data, identity, freshness, and retention. |
| [V03](../subplans/m8-3-v03-scoring-integrity.md) / [V06](../subplans/m8-6-v06-command-recovery.md) | Use independent before/after examples once their policies are decided; retain actual system evidence separately. |

M1.5's three documentation criteria are covered: E01–E36 state starting values,
commands, and expected outcomes or an explicit unresolved branch; the D11 register
keeps unanswered behavior blocked; examples preserve final points, deliberate
confirmation, team identity, and customized timeout allowances. This completes
the reviewable planning specification. Eighteen owner choices are recorded;
remaining D11 policies and implementation mechanics stay open. There is no
evidence of delivered application behavior.

Before implementation, record the relevant owner choices and technical dispositions,
meet G1/G2, and confirm T01/T04/T06 as applicable before their first tests. Future
behavior is delivered one red/green slice at a time under the existing TDD agreement.

## Focused D11 policy review, 2026-10-20

The owner requested the next step after M2.3's documentation research. The
dashboard recommended review of remaining D11 match policies. This follow-up
prepares concrete choices for that review; it does not reopen the completed
M1.5 documentation task, select application technology, or start application
implementation. The owner-confirmed Q16–Q18 remain in force; Q22–Q25 now record
four follow-up answers.

The first three questions were presented together with recommended and
alternative answers and a custom-answer field. **All three were answered on
2026-10-20:** preserve the margin at a cap conflict, allow totals-only baselines,
and pause rally scoring at a potential winner. The owner did not choose the
recommended cap-overrides-margin option. The later choices below remain
prepared for subsequent review, not included implicitly in these answers.
The additional cap-conflict question was also answered: require explicit
current-set continuation before allowing scoring beyond its cap (Q25).

| Review choice | Recorded choice or remaining recommendation | Status / affected behavior |
| --- | --- | --- |
| Hard-cap precedence — D11.2 | Preserve the winning margin; 30–29 at target 25/margin 2/cap 30 is not a potential winner. Require explicit current-set continuation before scoring beyond the cap; later sets keep their cap. | Q22/Q25 confirm live cap behavior; Q29 confirms cap-input restoration. Used by M3.1/M3.4; E16–E18, K01–K09, and R09–R12. |
| Mid-match starting totals — D11.3 | Permit known sets-won totals without earlier point scores. Keep those totals as an explicit baseline and record later completed sets separately; never invent earlier scores or winner order. | Q23 confirms ongoing totals-only baseline; Q28 confirms completed entry only from complete ordered valid history. Q31/Q32 confirm matching-prefix replacement and valid reviewed baseline corrections. Used by M3.1/M3.6; E20–E22, C01–C07, and B01–B12. Technical reconciliation mechanics remain open. |
| Rally input at a potential winner — D11.6 | Pause further ordinary rally scoring until confirmation or correction. Keep score correction available, including returning to a non-winning score. | Confirmed on 2026-10-20; Q24. Used by M3.2/M3.4. Does not choose next-set serve or confirmation storage mechanics. |
| Undo scope and repeat — D11.4 | Allow Undo of the latest accepted match mutation, including score, serve, timeouts, names, display swaps, set confirmation, and coherent historical corrections. Exclude media/account actions and confirmed reset. A successful Undo consumes the opportunity; a second Undo does nothing until a new accepted match mutation. | Confirmed on 2026-10-21; Q26. One-step depth and restart retention remain confirmed under Q18. |
| Watch Undo permissions — D11.4 | Permit the watch to undo the latest mutation only when that action is in its allowed match controls. If the latest mutation edits names/history, direct Undo to a phone. Never skip it to undo an older scoring action. | Confirmed on 2026-10-21; Q27. The watch remains view-only for names/history and other restricted fields. |
| Historical correction interaction — D11.5 | Review affected recorded results together on a phone/tablet, preview derived totals/current-set/phase, and commit one coherent correction after validation. Preserve recorded play on rejection; add no automatic deletion. If a valid correction to a completed match leaves neither team with enough wins, reopen the required next set at 0–0 with full timeouts and no active server until selected. | Confirmed on 2026-10-21; Q30. History correction remains phone/tablet-only; corrected history, identities, configuration, and display order persist, and streaming is independent. |
| First server for a new set — D11.6 | Require explicit team selection before ordinary rally input; show no active serving marker while unset and never infer service from earlier play. | Confirmed under Q33 on 2026-10-21; S01–S07 apply the policy. No rotation or coin-toss feature is proposed. |
| Reset contents and authority — D11.6 | After deliberate confirmation on a phone/tablet, retain teams/names/codes, copied settings, and display order; clear baseline/history/points/sets/Undo/continuation, restore full timeouts, and leave serve unset. Keep media independent; deny watch reset. | Confirmed under Q34/Q35 on 2026-10-21; S08–S14 apply the policy and identify technical proposals. Fresh identity/epoch and revision/storage mechanisms remain open. |

### Supplemental review cases

R01–R04 apply confirmed portions of Q22–Q24; R09–R12 apply Q25. R05 now applies
Q31/Q32 baseline accounting, and R06/R07 apply Q26/Q27. R08 now applies Q34/Q35
reset policy while retaining identity/epoch as a technical proposal.
These are written examples, not executable tests or delivered behavior.

| ID | Starting state | Action | Observable result and basis |
| --- | --- | --- | --- |
| R01 | Set 1, target 25, margin 2, cap 30, points 29–29 | Award A a rally | Confirmed Q22/Q25: 30–29, serve A, no potential winner; pause ordinary rally input until explicit current-set continuation or a correction. Do not confirm a one-point win. |
| R02 | Uncapped set 1 at 25–23 for A, serve A, potential result not confirmed | Submit another ordinary rally command | Confirmed Q24: reject without changing score, serve, totals, or history; allow confirmation or a score correction. Enforce on the phone even if a stale controller offers its rally button. |
| R03 | R02 | Correct A's score to 24 | Confirmed Q16/Q24: 24–23, serve remains A, no potential winner; ordinary rally input becomes available again. |
| R04 | Ongoing best-of-5 setup; earlier scores unknown | Enter baseline sets 1–1 and current points 7–5 | Confirmed Q23: retain baseline 1–1 without fabricated earlier scores or win order. Proposed accounting represents the active regular set as set 3 at 7–5. |
| R05 | R04; record and confirm A's set 3 win by 25–20 | Show match totals and history | Totals 2–1, active set 4 at 0–0 with full configured timeouts; history contains the recorded set 3, and earlier totals stay explicitly baseline-only. Q33 leaves serve unset until explicit selection before ordinary rally input. |
| R06 | A phone name edit is the latest match mutation; the preceding mutation was B's point | Request Undo from the watch | Explain that the latest edit requires a phone; change nothing. Do not undo the older B point or infer watch name-edit permission. |
| R07 | The latest mutation was a sideout point, and its Undo has just succeeded | Request Undo again before a new match mutation | Leave the restored state unchanged; do not perform Redo or reach an earlier mutation. Media/status updates do not create a new match Undo opportunity. |
| R08 | Active match with a custom timeout allowance of 3, existing baseline/history, swapped display order, and an active stream | Confirm reset on an authorized phone/tablet under Q34/Q35 | Set 1 at points/sets 0–0, timeouts 3–3, no baseline/history/Undo/continuation, retained teams/copied settings/display order, and unset serve. Publishing is unaffected; watch reset is denied. New identity/epoch remains a technical proposal. |
| R09 | R01, current-set continuation not yet authorized | Open continuation confirmation and cancel | Confirmed Q25: preserve 30–29, serve A, and the unresolved cap conflict, with ordinary rally input still paused. Do not authorize continuation or change later-set configuration. |
| R10 | R01, current-set continuation not yet authorized | Confirm "continue win by two" | Confirmed Q25: points stay 30–29 with serve A and no potential winner; ordinary scoring may now continue beyond this set's cap. Keep the margin of 2 and later-set caps unchanged. |
| R11 | R10 with current-set continuation authorized | Award B a rally, then A, then A | Confirmed Q22/Q24/Q25: 30–30, then 31–30, neither a potential winner; 32–30 is a potential winner. Pause further ordinary rally scoring until confirmation or correction. |
| R12 | R11, best-of-5 set 1 at 32–30, with A not yet the match winner | Confirm set 1 and start set 2 | Confirmed Q25 scope: record 32–30 and use the configured cap of 30 for set 2. The set 1 exception does not carry forward. Apply Q33: no server until explicit selection, with ordinary rally input blocked. Other next-set fields follow the PRD. |

### Technical review and remaining handoff

- Keep one global accepted match order across local, Android, and watch edits.
  Scope a proposed Undo to its reviewed current mutation; never reinterpret a
  stale Undo as permission to undo a different later action. Exact revision and
  receipt representation remains D04/D11/D13 work.
- Baseline totals cannot establish the order or point scores of unrecorded
  earlier sets. If a historical correction needs those facts, request history
  instead of fabricating it. A baseline must not also count a recorded result.
- A correction draft is not an accepted scoreboard update. Validate the complete
  resulting sequence and apply its derived values together; retain the prior
  committed snapshot on rejection. No destructive fallback is approved.
- Keep numeric receipt lifetimes, expiry clocks, pruning, storage bounds, and
  duplicate handling in M2.5/M2.7 and D04/D11.7. Owner scoring choices cannot
  establish their feasibility, and no arbitrary timing value is selected here.
- Retain accepted current-set continuation with the match so restart or remote
  resynchronization cannot silently remove or grant the exception. Its atomic
  representation is D04/M3.7 work. Whether Undo covers this confirmation still
  belongs to the open Undo-scope policy; the selected exception never applies
  to a future set merely because its score or revision resembles an earlier one.
- M3.1/M3.4/M3.5/M3.6 and the watch task remain Not started. Owner answers must
  be recorded before dependent behavior; G1/G2 and confirmation of T01/T04/T06
  still apply before application tests or implementation.

This review now retains four additional owner choices and exposes remaining
cross-device interactions. Q22–Q25 are reflected in the PRD and affected example
labels. It does not close D11, complete another task, open an implementation
gate, or supply physical/application evidence. Initial/restored cap-input validation and
the other unanswered choices remain blocked before their dependent behavior.

Pre-answer follow-up documentation validation on 2026-10-20: **PASS**, exit code 0,
using `node plan/tools/check-planning-package.cjs`. All 68 Markdown documents,
1,070 local links, 246 heading targets, 53 tasks/index rows, 159 dependency
edges, eight Mermaid pairs, and nine locked skills passed the mechanical
checks. Separate checks retained unique sequential E01–E36 and R01–R08 and
unchanged task counts of 7 complete, 1 in progress, and 45 not started.
AGENTS.md and skills-lock.json match their pre-work hashes. The PRD was
intentionally updated only to point to the pending review; no owner answer or
application result was recorded. The older validation below remains dated
evidence for the original 2026-10-18 specification.

## Post-answer validation, 2026-10-20

The four follow-up answers are recorded as unique PRD Q22–Q25 and applied to
the specification, task handoffs, master plan, architecture, dashboard, and
matching lifecycle diagram. D11 has seven confirmed owner choices; its
remaining input-validation, controller/Undo, history, serve, reset, and retention
details stay open. Other confirmed requirements and task states are preserved.

The existing documentation checker passed with exit code 0 and no errors:
68 Markdown documents, 1,074 local links, 248 heading targets, 53 task/index
records, 159 dependency edges, eight Mermaid pairs, and nine locked skills.
Separate reconciliation passed for E01–E36, R01–R12, unique Q1–Q25, unchanged
7 complete / 1 in progress / 45 not started, and 13 open aggregate records.

The lifecycle SVG parsed as valid XML and matched the updated source labels;
an 1800-pixel temporary PNG was visually inspected. Rendering used the existing
Node.js runtime, installed pretty-mermaid tooling, and `github-light` theme.
AGENTS.md and skills-lock.json match their pre-work hashes. The PRD changed
intentionally to record actual owner decisions; no application code, tests,
dependencies, or physical/provider results were added.

## Documentation validation

Validated on 2026-10-18 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no reported errors. The check covered 66 Markdown
documents, 1,020 local links, 229 heading targets, 53 task files/index rows,
159 dependency edges, eight diagram pairs, and nine locked skills.

A separate documentation reconciliation passed: E01–E36 are unique and in
sequence, with basis, starting state, command, and expected result present;
all six completed tasks have checked acceptance lists; all nine milestone rows
match their task records; the dashboard shows 6/53 complete, M1 at 5/6,
47 not started, and 13 open aggregate decision records. PRD Q16–Q18 are present.

Manual review checked the sideout/subtraction/Undo distinction, nonnegative and
custom timeout bounds, deuce and deciding targets, cap proposals, final-point
retention, consistent winner changes, rejection of impossible later play,
stable identity, and separation of confirmed answers from remaining proposals.
These are documentation checks; no application, persistence, protocol, physical
device, or received-broadcast result is asserted.

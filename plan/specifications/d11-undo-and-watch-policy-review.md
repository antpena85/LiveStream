# D11 Undo scope and watch permissions

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Two owner decisions confirmed; written examples apply the selected policy.  
Decision: [D11.4](m1-5-match-correction-specification.md#d11-policy-register)  
Authority: [FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[device control matrix](../../PRD.md#24-device-control-matrix), Q18 and Q25  
Companion: [M1.5 specification](m1-5-match-correction-specification.md)

## Review scope

This review settled Undo action scope and watch permissions. The owner had
already confirmed one-step Undo of the latest match edit, retained across app
restart (Q18); Q26/Q27 complete the scope/repeat and watch-authority decisions.

The installed PRD and volleyball-rules skills cover this review. Local skill
discovery found both; no additional skill or dependency is needed. Product
requirements and the owner's answers take precedence over skill examples.
No new platform capability or official volleyball rule is asserted here.

## Owner decisions recorded on 2026-10-21

The owner selected the recommended answer to both questions. These answers are
recorded in [PRD Q26/Q27](../../PRD.md#53-confirmed-scope-decisions).

| Choice | Recommended | Alternative | Answer |
| --- | --- | --- | --- |
| Undo scope and repeat | Cover all otherwise permitted accepted match edits: points, serve, timeouts, names/codes, display swaps, starting-value corrections, set/match confirmation, valid history corrections, and current-set cap-continuation confirmation. A successful Undo consumes the opportunity; another Undo does nothing until a new accepted match edit. | Limit scope to points, serve, timeouts, and set/match confirmation. Other accepted match edits block Undo rather than being skipped. | **Confirmed: recommended answer, PRD Q26.** |
| Watch Undo authority | Permit Undo when the latest edit is within the watch's allowed match controls, regardless of which device made it. For a restricted edit, show that a phone is required; never skip to an older edit. | Permit inverse actions for every Undo-eligible match edit, including phone-only edits; or restrict watch Undo to its own latest edit. | **Confirmed: recommended answer, PRD Q27.** |

The selected scope excludes stream/account actions and confirmed match reset.
The review concerns edits to an existing match; it does not propose Undo of
initial match creation, a saved preset library, or unrelated application settings.
Including an action in Undo does not authorize making that action where it is
otherwise unavailable. In particular, it adds no mid-match preset editor,
historical-correction workflow, or watch cap-continuation control.

## Confirmed action coverage

This table applies the confirmed Q26/Q27 answers.
All commands still require an authorized, synchronized controller.

| Latest accepted action | Phone/tablet Undo | Watch Undo | Restored match values |
| --- | --- | --- | --- |
| Point addition/removal or score correction | Eligible | Eligible within approved scoring controls | Affected scores, recorded serve, and resulting match phase. Point subtraction alone still leaves serve unchanged under Q16. |
| Manual serve edit | Eligible | Eligible | Previous serving possession. |
| Timeout consumption/correction | Eligible | Eligible | Previous remaining counts within the configured allowance. |
| Display-side swap | Eligible | Eligible | Previous display order; stable team identities retain their data. |
| Set/match confirmation | Eligible | Eligible within the approved set-confirmation control | Previous active set, points, timeouts, history, totals, phase, and server as actually recorded. |
| Names/codes or historical-result correction | Eligible when the edit itself was permitted and valid | Use a phone | Affected metadata or coherent history and derived current state. No unknown history is invented. |
| Starting-value/baseline correction | Eligible when otherwise permitted | Use a phone unless a separate control is approved | Recorded baseline and all affected derived values, without double counting. |
| Current-set cap-continuation confirmation | Eligible under Q26/Q36 | Use a phone under Q36; never skip the restricted edit | Prior continuation authorization and phase. With no later match edit, a cap conflict becomes paused again. |
| Confirmed match reset | Excluded; no Undo across the reset boundary | Reset unavailable under later Q35 | Q34 settles retained/cleared contents; technical identity mechanisms remain D11.6 work. |
| Camera, mute, stream, provider/account, or connection/status change | Outside match Undo; preserve the current opportunity | Same | Undo never reverses these actions. |
| Scoreboard placement change | Outside match Undo under Q40; preserve the current opportunity | Undo of the latest permitted match edit remains allowed; no direct placement control | Keep the currently selected position when match values are restored. |
| Rejected, canceled, duplicate, or nonmutating request | Creates no new accepted match edit | Same | Preserve committed match state and any existing Undo opportunity. Exact receipt handling remains technical work. |

Later Q37–Q39 settle placement defaults, persistence, and phone/tablet-only
controls in the [placement/control review](d10-placement-control-policy-review.md).
Q40 subsequently excludes placement from match Undo: preserve the existing
opportunity, and keep the selected position when Undo reverses the latest match
edit. PU01–PU08 cover this policy without granting direct watch placement access.
An intervening placement change neither hides a permitted match edit nor makes
a restricted one eligible for watch Undo. Including a valid historical correction in
Undo does not itself specify the correction/reopening interaction.
Later Q30–Q35 settle correction/reopening, baseline/history, first-server, and
reset policies in their linked M1.5 reviews. The [serve/reset review](d11-new-set-serve-and-reset-policy-review.md)
records Q34/Q35's reset contents and phone/tablet authority; this does not alter
the confirmed no-Undo boundary.

## Worked cases under the confirmed policy

U01–U12 are documentation examples under the confirmed Q26/Q27 policy. They
are not application tests or evidence that behavior works. Revision labels
describe the proposed freshness check; no storage schema is selected.

| ID | Starting state | Action | Expected observable result under the confirmed policy |
| --- | --- | --- | --- |
| U01 | Points 10–9, serve A, timeouts 1–2; phone awards B a rally, reaching 10–10 with serve B | Watch reviews and undoes that latest point | Restore 10–9, serve A, timeouts 1–2. The action's origin on a phone does not block watch Undo. |
| U02 | The latest edit changes A's name from Eagles to Falcons on a phone; the preceding edit was B's point | Watch requests Undo | Show that a phone is required; preserve Falcons and the score. Do not skip the name edit. A phone may still undo it. |
| U03 | U02; watch Undo was denied, with no intervening accepted match edit | Phone reviews and undoes the name change | Restore Eagles; preserve all scoring values. The denied watch request did not consume the opportunity. |
| U04 | A sideout point has just been undone successfully | Request another Undo, then restart and request Undo again before any new match edit | No further match change or Redo. Restart does not recreate a consumed opportunity. |
| U05 | Latest match edit is B's timeout, reducing remaining counts from 2–2 to 2–1; the app restarts | Refresh from the streaming phone and Undo | Restore 2–2 with the previous points/server unchanged. Keep media state independent. Q18 requires restart retention; atomic storage remains unverified. |
| U06 | A nonfinal set was confirmed at 26–24 with timeouts 1–0 and totals 0–0; set 2 is untouched at 0–0, totals 1–0 | Undo that latest confirmation from an allowed controller | Restore set 1 at 26–24, timeouts 1–0, totals 0–0, potential winner A, and its recorded server. Q24 keeps ordinary rally scoring paused pending confirmation or correction. |
| U07 | Best-of-3 final set 3 was confirmed at 16–14, totals 2–1, final timeouts 1–0; no later match edit | Undo the latest final-set confirmation | Restore set 3 at 16–14, totals 1–1, timeouts 1–0, and potential match winner A. No extra set is created and stream state does not change. |
| U08 | Target 25, margin 2, cap 30, score 30–29; latest match edit confirmed current-set continuation; no rally followed | Phone undoes that confirmation | Preserve 30–29 and the recorded server; remove continuation authorization and return to the unresolved cap conflict with ordinary rally input paused. Later-set configuration is unchanged. Q36 permits this confirmation/Undo on permitted phones/tablets only; the watch cannot skip it. |
| U09 | U08's continuation was confirmed, then B's rally committed at 30–30 with serve B | Undo once | Restore 30–29 and the prior server while retaining continuation authorization. The latest mutation was the rally; never also undo the earlier confirmation. A second Undo cannot reach it. |
| U10 | A controller reviewed Undo of A's point at r10; B's timeout commits at r11 | Submit the reviewed Undo against r10 | Reject the stale request without reversing the timeout or point. Refresh and show the actual latest edit before a new Undo request. Exact revision/receipt mechanics remain proposed. |
| U11 | Latest match edit is A's point; camera, microphone mute, and stream status then change | Review and Undo the point | Restore the prior scoring values and recorded server while preserving the current camera, mute, and stream state. These changes neither replace nor consume match Undo. |
| U12 | Point command C was accepted once and later undone; its original acknowledgment had been lost | Retry C | Do not reapply the point or create a new Undo opportunity. Return the prior command outcome with fresh match state under the eventual receipt protocol. Receipt retention is independent of Undo retention. |

## Handoff and remaining decisions

- [M3.5](../subplans/m3-5-undo-and-confirmed-reset.md) consumes the selected
  scope, repeat behavior, and before/after examples. Its implementation remains
  Not started; later Q34/Q35 settle reset contents and phone/tablet authority.
- [M7.2](../subplans/m7-2-watch-match-controls.md) and
  [M8.10/V10](../subplans/m8-10-v10-full-controller-matrix.md) consume the chosen
  permission rule, including cross-device edits and explicit denials.
- [M3.7](../subplans/m3-7-atomic-persistence-and-restart.md) and
  [M6.2](../subplans/m6-2-ordered-idempotent-commands.md) still need atomic
  state/Undo/receipt storage, current-session authorization, stale-command
  handling, and bounded retention. An Undo is a fresh accepted change; its
  own retry must not apply again or create a new Undo opportunity.
- Later Q28–Q36 settle completed entry, cap input, history/baseline, new-set serve,
  reset, and continuation controller policies in their separate reviews. The
  [cap-permissions review](d11-cap-continuation-permissions-review.md) records
  Q36's phone/tablet-only confirmation and latest-confirmation Undo; the watch
  cannot skip that restricted edit. Technical storage/identity/receipt choices remain open.

The selected answers are synchronized to the PRD, M1.5, affected task plans,
architecture, and dashboard. 
G1/G2 and confirmation of T01/T04/T06 remain prerequisites. M1.5 stays complete
for documentation; these owner decisions do not complete an application task or
advance a gate.

## Documentation validation

Validated on 2026-10-21 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no errors. The checker covered 69 Markdown documents,
1,090 local links, 252 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills.

Separate reconciliation before the owner answers confirmed unique sequential U01–U12, unchanged
E01–E36/R01–R12, unique Q1–Q25, two pending answers, 13 open aggregate decision
records, and task counts of 7 complete / 1 in progress / 45 not started.
Completed-task checklists remain checked; not-started tasks remain unchecked.
AGENTS.md and skills-lock.json retain their pre-work SHA-256 hashes. The PRD
then changed only to link this pending review; no new owner answer had been
recorded at that validation point.

Manual review checked restoration of serve/timeouts/phase, final-set retention,
watch denials without skipping older edits, cap-continuation authorization,
consumed Undo across restart, media independence, and the distinction between
Undo retention and duplicate protection. These are documentation checks; no
application code, tests, dependencies, or physical/provider results were added.

## Post-answer synchronization

On 2026-10-21, the owner selected both recommended answers. Q26/Q27 now record
the confirmed scope/repeat and watch-authority policies. This review, the PRD,
M1.5, affected task plans, architecture, master plan, and status dashboard were
synchronized. No task count, aggregate decision-record count, or delivery-gate
status changed; implementation remains unauthorized.

The documentation checker passed after synchronization: 69 Markdown documents,
1,093 local links, 254 heading targets, 53 task/index records, 159 dependency
edges, eight diagram pairs, and nine locked skills. Reconciliation confirmed
unique Q1–Q27; U01–U12 remain unique and sequential; task counts remain
7 complete / 1 in progress / 45 not started. These are documentation checks,
not application, device, provider, or broadcast evidence.

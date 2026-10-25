# D10 numeric fit and overflow policy review

Prepared and answered: 2026-10-23 (America/New_York)  
Status: Two owner choices confirmed as PRD Q44/Q45; technical limits and rendering evidence remain open.  
Decision: [D10.4–D10.7](m1-6-overlay-display-specification.md#d10-display-choices)  
Authority: [PRD FR-04](../../PRD.md#fr-04-display-an-indoor-volleyball-broadcast-scoreboard),
[FR-05](../../PRD.md#fr-05-enter-and-correct-match-information),
[control matrix](../../PRD.md#24-device-control-matrix)  
Companion: [M1.6 scoreboard specification](m1-6-overlay-display-specification.md)

## Existing requirements and scope

Show complete committed point values, compact sets-won totals, and remaining
timeouts labeled `T/O`, with both accepted abbreviations and serving ownership.
Keep each team's values together through a display swap. Q41/Q42 allow codes
of 1–5 uppercase A–Z/digit characters, including explicitly confirmed matching
codes; matching text never merges identities. Q19/Q37–Q40 retain the five
placement presets, phone-specific placement preference, and its exclusion from
match Undo.

The standard no-cap presets have no approved two-digit scoring ceiling.
Customized timeout allowances and sets-to-win also have no approved display
digit limit. Neither the example `25` nor a two-timeout illustration establishes
a product maximum. A sporting point cap and a storage/rendering capacity are
different constraints; this review chooses neither a machine integer type nor
a new scoring cap.

The existing M1.6 proposal renders complete decimal numbers without clipping,
ellipsis, wrapping to a smaller value, or replacing a value with `99+`. Finite
video bounds cannot accommodate arbitrarily long text at a fixed readable size.
Numeric ranges, font metrics, maximum block coverage, and the D07 quality floor
need recorded dispositions and actual output evidence.

## Owner decisions recorded on 2026-10-23

Both questions were presented with a custom-answer field. The owner selected
the recommended adaptive layout and the alternative that stops broadcasting
while preserving the full new match value. [PRD Q44/Q45](../../PRD.md#53-confirmed-scope-decisions)
record that exact combination. The live-edit rejection recommendation was not
selected.

| Choice | Recommended proposal | Alternative | Status |
| --- | --- | --- | --- |
| NF1 — Growth when numbers gain digits | Widen or rearrange the complete scoreboard within reviewed safe bounds, keeping full numbers and the reviewed readable text size. More digits may cover more court. | Reserve larger numeric fields in a fixed layout; extra area is occupied even for small values. Capacity still needs review. | Confirmed: recommended, Q44. |
| NF2 — A number cannot fit after layout adjustment | Block broadcast start, or reject the live edit with a clear error and preserve the last accepted match/overlay. The operator can stop broadcasting to continue scoring off-air. This protects broadcast continuity but can pause score entry. | Stop broadcasting with a clear notice, preserve the full new match value, and let scoring continue off-air. This prioritizes score entry at the cost of broadcast continuity. | Confirmed: stop alternative, Q45. |

Q44 does not approve arbitrary dragging, manual resizing, new placement presets,
a theme system, or reducing type below the reviewed readable size. Q45 is the
owner-selected controlled-stop exception for this diagnosed numeric capacity
failure. It does not permit rejecting otherwise valid score entry to keep
publishing, or stopping for ordinary numbers that fit. The existing reliability
targets continue to apply to supported readable configurations.

## Proposed fit assessment

These technical planning criteria apply the confirmed policies; exact D10/D07
limits and mechanisms remain proposals. They select no renderer or API.

1. Validate the match under its copied configuration before assessing fit.
   Negative values, impossible histories, or unapproved cap continuation remain
   match errors; fitting their text does not make them valid.
2. Measure the complete candidate projection: both codes/point values, compact
   sets pair, both `T/O` counts, and serving slots. Use actual selected-font
   glyph bounds and spacing; digit count alone is insufficient alongside
   five-character wide codes.
3. Under Q44, allow reviewed wider or rearranged layouts
   inside the selected anchor's bounds. Keep team fields associated and preserve
   the compact sets pair's order, labels, and serving cues. Layout dimensions
   are not a new placement preference or match edit.
4. Review all five anchors at every selected output size, including the D07
   floor. Decide safe insets, maximum block width/height or coverage, type sizes,
   spacing, and acceptable layouts. The existing 5% margins remain candidates.
5. Cover full configured timeout replenishment and sets-won totals as well as
   current values. New-set/reset transitions can widen `T/O` from `0` to the
   full allowance while points become smaller. No-cap points still need an
   explicit supported representation and failure policy.
6. Separate static artwork, local rendering, encoded frames, and received video.
   Width calculations alone do not establish actual broadcast readability.

## Confirmed failure behavior and proposed technical handoff

After Q44's layout adjustment, if an otherwise valid numeric result cannot fit
legibly within the reviewed bounds, Q45 requires a clear notice and a stop to
active broadcasting. Preserve the complete new match value and keep permitted
scoring/corrections usable off-air. Do not reject that valid numeric update,
roll back to the old number, truncate it, or omit another required field to
keep publishing. An already unfit off-air match cannot start a readable
broadcast; direct the operator to resolve fit before a subsequent start.

Apply the same outcome to numeric results of permitted point/timeout edits,
set confirmation, history corrections, Undo, and reset. Match validation and
controller authorization still run: Q45 does not accept impossible histories,
negative values, stale actions, or restricted watch controls. If a valid
numeric Undo is accepted, restore the recorded match and consume Undo under
Q26; keep placement under Q40 and stop publishing if the restored numbers
cannot fit. A confirmed reset retains/clears fields under Q34, including Undo.

The command and session designs must distinguish a successful match edit from
the resulting stream stop. Local and remote controls need the full new match
and the visible stop reason, without a misleading rejected-point result that
invites duplicate entry. A retry must never count the accepted edit twice.
Atomic match/Undo/receipt storage, revisions, and retry retention remain D11
technical decisions.

Durable match acceptance, publisher shutdown, intermediate frames, controller
results, process recreation during shutdown, and shutdown-failure recovery need
a defined sequence before implementation. Storing a match and stopping an
external stream are not asserted to be one atomic transaction. Do not report
"stopped" until publishing has stopped; exact timing and fallback require media
evidence. Q45 does not promise that the destination receives a final frame
showing the unfit number. Preserve the full local match regardless.

Off-air scoring/correction retains full valid numbers within the eventual
supported storage range. A known fit failure requires resolution before a
subsequent start on any permitted control surface. Resolving it does not
automatically restart publishing; normal start confirmation/readiness apply.
Q43 code recovery remains separate and never silently repairs original codes.
Reset and ordinary fitting match edits keep broadcasting independent.

## Quality changes and unexpected renderer failures

A quality change is not an incoming match edit. Q45 does not by itself choose
the adaptation algorithm or its quality floor. D07/D10 should evaluate modes against
the supported layout/range envelope before choosing the quality floor. Bitrate
changes need received-video review even when pixel dimensions stay constant.

If upload capacity cannot sustain any readable supported mode, visible state,
retry/stop policy, and recovery remain D07 decisions. No permission to hide the
scoreboard, freeze stale values as current, reduce type indefinitely, or stop
on an ordinary quality change is inferred from Q44/Q45. A confirmed numeric
fit failure follows Q45; insufficient bitrate without such a failure still
requires the D07 recovery disposition. Unexpected renderer or
encoder failures need a separate recovery disposition; they do not prove a
valid number exceeds a supported range.

## Worked planning cases

These are written outcomes applying confirmed policy, not executed tests.
Technical mechanisms remain proposals. Values illustrate cases rather than limits; an
"unfit" fixture must eventually be established against the decided layout.

| ID | Setup and action | Expected outcome / pending condition |
| --- | --- | --- |
| N01 — No-cap digit growth | Active no-cap set at 99–98, target 25/margin 2; award the leading team a rally | Candidate is 100–98 with that team's serve. Apply Q44's wider/rearranged layout and show complete values when fit is established; pause further ordinary rallies at the potential winner under Q24. A diagnosed fit failure follows Q45. |
| N02 — Custom timeouts | Custom allowance 12, current remaining counts 10–0 | Render `T/O 10` and `T/O 0` in full. Twelve is illustrative, not a selected maximum. |
| N03 — Timeout replenishment | Valid nonfinal confirmation or confirmed reset restores a multi-digit allowance | Preserve the entire valid resulting state, including full timeouts. If its numbers cannot fit after Q44 adjustment, stop publishing with a notice under Q45; do not partly advance/reset or discard the new match values. |
| N04 — Custom set totals | Valid custom format with sets-to-win 12 and derived totals 10–9 | Preserve the compact ordered `10 - 9` pair. Do not clip to one digit or impose the standard best-of-5 limit. Actual fit remains to be shown. |
| N05 — Long matching codes | Explicitly accepted `WWWWW`/`WWWWW`, asymmetric valid points/timeouts, known server | Measure both complete groups with independent values. Q42 permits the text; no team merging or code shortening to fit. |
| N06 — Anchors and swaps | Fitted larger-value block moves through all five presets; teams then swap order | Stay inside decided bounds and move all associated values/serve with each team. Q44 does not change the saved anchor or Q40 Undo policy. |
| N07 — Unfit start | Off-air valid match has an established unfit numeric projection; start requested locally, on Android remote, or watch | Block every start path with an actionable reason; retain the full match and scoring. Existing start confirmation and code/capture/provider readiness remain necessary. |
| N08 — Unfit live rally | Current committed projection fits; an otherwise valid rally would not fit after Q44 adjustment | Preserve the full new point/serve and normal Q26 Undo result. Stop publishing with a clear notice under Q45; continue scoring off-air. Do not reject the valid point or promise a final received frame with the unfit value. |
| N09 — Scoring after the stop | N08's complete match is retained off-air | Subsequent valid scoring/corrections remain available. A correction that makes the layout fit does not automatically restart the broadcast; normal start confirmation/readiness apply. |
| N10 — Undo after code repair | Q43 repair is latest edit; Undo would restore a nonconforming code while live | Q26/Q27 restrict this Undo to a phone. Q43 alone selects no live automatic stop. Restored-code rendering/live acceptance need an explicit disposition; numeric failure cannot silently rewrite the code. |
| N11 — Numeric Undo | Latest permitted edit reduced a large value; valid Undo restores numbers established unfit while live | Restore the full recorded match, consume Undo under Q26, keep current placement under Q40, and stop broadcasting with a notice under Q45. Restricted watch edits remain restricted. |
| N12 — Quality transition | Current values fit the present output; a smaller candidate output fails fit criteria | D07/D10 must choose a readable output envelope and adaptation behavior. Do not select a known unfit candidate as a normal supported downgrade; if a numeric fit failure occurs, Q45 preserves the match and requires a visible stop. Insufficient bitrate/retry recovery remains open. |
| N13 — Restart/recovery | Off-air saved match has full valid numbers that do not fit the selected layout | Preserve data and permitted scoring/corrections; block publishing until a readable configuration or reviewed correction is available. Never reduce stored values to fit or automatically resume publishing on restart. |
| N14 — Accepted remote retry | Controller retries N08 after losing its acceptance/stop response | Apply the point once and return the full authoritative match/stream result. Stream shutdown must not turn an accepted edit into a new point on retry; receipt durability, expiry, and identity remain D11 work. |

## Handoff and remaining work

| Work | Handoff / remaining condition |
| --- | --- |
| [M3.9](../subplans/m3-9-local-controls-and-scoreboard-projection.md) | Apply Q44/Q45: adaptive complete values; preserve the valid numeric edit and continue off-air after a diagnosed fit stop. No clipping or partial match commit. |
| [M4.3](../subplans/m4-3-encoded-scoreboard-composition.md) | Complete-field fit across selected layouts, five anchors, fonts, and modes; encoded-frame inspection remains required. |
| [M2.6](../subplans/m2-6-adaptive-quality-policy.md) | Define the D07/D10 readable output envelope and insufficient-upload recovery separately from live-edit failure. |
| [M6.2](../subplans/m6-2-ordered-idempotent-commands.md) / [M7.2](../subplans/m7-2-watch-match-controls.md) | Equivalent authorized local/Android/watch outcomes; resolve revisions/receipts and retry behavior before implementation. |
| [V02](../subplans/m8-2-v02-received-overlay.md) | Received-video evidence for multi-digit values, wide codes, custom timeouts/sets, swaps, and quality transitions; retain the approved 20-change script. |

Owner answers are recorded. Required inputs remain reviewed geometry,
typography, numeric capacity, durable stop/recovery contracts; D07
quality/routing/retry choices; and actual
encoded/received evidence. No numeric maximum, output mode, dependency,
prototype, or executable application test is established. M1.6 remains complete
for documentation; D10 stays open and no delivery gate advances.

## Documentation validation

On 2026-10-23, `node plan/tools/check-planning-package.cjs` passed with exit
code 0 and no errors: 78 Markdown documents, 1,286 local links, 296 heading
targets, 53 task records, 159 dependency edges, eight diagram pairs, and nine
locked skills. The checker validates documentation structure and links, not
application behavior or rendering.

Separate reconciliation passed for unique sequential Q1–Q45, N01–N14, twelve
confirmed D10 display decisions, and all nine milestone totals. Task counts
remain 7 complete, 1 in progress, 45 not started, and 0 unverified; all thirteen
aggregate decisions remain open. Completed task checklists/evidence references
and the selected Q44 recommendation/Q45 stop alternative were reviewed.
AGENTS.md and skills-lock.json retain their pre-work SHA-256 values.

Manual consistency review covered complete match acceptance versus publisher
shutdown, numeric Undo consumption and reset contents, rejected invalid or
unauthorized edits, accepted remote retry deduplication, unresolved code-recovery
rendering, and D07's separate adaptation/recovery decisions. No executable
application tests, devices, encoder, provider account, or received broadcast
were exercised.

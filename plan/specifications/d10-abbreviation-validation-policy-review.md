# D10 abbreviation validation policy

Prepared and answered: 2026-10-21 (America/New_York)  
Status: Three owner decisions confirmed as PRD Q41–Q43; technical/readability details remain open.  
Decision: [D10.2/D10.7](m1-6-overlay-display-specification.md#d10-display-choices)  
Authority: [PRD Q20](../../PRD.md#53-confirmed-scope-decisions),
[device control matrix](../../PRD.md#24-device-control-matrix)  
Companion: [M1.6 scoreboard specification](m1-6-overlay-display-specification.md)

## Confirmed constraints and review scope

Q20 already requires editable 1–5-character team codes with entry validation
instead of silent truncation. Full names remain separate and editable in match
information. Phones/tablets may edit codes; the watch is view-only. Stable team
identities own scores, history, timeouts, and serve regardless of code or display
order. A code edit does not rename the team's full name or transfer its match data.

Q26 includes accepted code changes in one-step match Undo; Q27 requires a phone
for Undo of that restricted latest edit and never skips it to reach an older
point. Q40 keeps the current placement when a match edit is undone. This review
does not change those requirements or add watch code editing.

Q41 selects the restricted character set and makes the length rule concrete:
count the final 1–5 letters/digits shown in the reviewed code. Q42 permits the
two teams to use matching codes after a warning and explicit confirmation.
International letters and typed-case preservation were offered but not selected.
No counting library, font, platform API, storage schema, or numeric layout limit
is selected by these owner policies.

## Owner decisions recorded on 2026-10-21

The question picker offered the following choices plus a custom-answer field.
The owner selected the recommended character rule and the alternative permitting
duplicates with a warning and explicit confirmation. The answers are recorded as
[PRD Q41/Q42](../../PRD.md#53-confirmed-scope-decisions).

| Choice | Recommended answer | Alternatives | Status |
| --- | --- | --- | --- |
| Character set and casing | Letters A–Z and digits 0–9, displayed uppercase. Trim outside spaces and show lowercase input as uppercase before saving. Reject internal spaces, punctuation, emoji, and other characters. | International letters/digits with typed case and visible-character counting; or A–Z/digits with typed case. Both alternatives trim outside spaces and reject internal spaces/punctuation/emoji. | Confirmed: recommended, Q41. |
| Duplicate team codes | Reject codes that match the opponent's code without regard to letter case; require distinct abbreviations. | Allow duplicates after a warning and explicit confirmation, while keeping stable team identities separate. | Confirmed: alternative allowing duplicates, Q42. |

The selected restricted character rule provides a small, predictable code repertoire
while leaving full names unrestricted by this abbreviation policy. It still
needs actual font/rendering and received-video review; uppercase and five
characters do not by themselves prove that the overlay is readable.

## Confirmed behavior and technical handoff

1. Review the candidate after trimming outside spaces and converting lowercase
   a–z to A–Z. Other characters are rejected rather than transliterated or
   expanded into permitted letters. Show the exact proposed on-air code before the
   operator saves it; do not silently change the committed code as typing occurs.
2. Accept only 1–5 letters A–Z or digits 0–9 in that candidate. Reject a blank,
   overlong, or unsupported candidate with a field-specific explanation. Keep
   the committed code and other match values on rejection; never truncate it.
3. Compare the prepared candidate against the other team's accepted code without
   regard to case. If they match, warn that both broadcast codes will be the same
   and require explicit confirmation before accepting the changed code. Canceling
   or declining keeps the existing codes and match Undo opportunity. Do not merge
   the teams or compare a team's unchanged code against itself as a new collision.
4. An accepted changed code uses the existing phone/tablet match-edit path and
   becomes the latest Undo-eligible edit under Q26. Rejected/canceled/unchanged
   candidates create no accepted match edit and preserve any existing opportunity.
5. Preview, controllers, and outgoing composition use the same complete accepted
   code with its stable team identity. Watch code-edit requests remain denied;
   the watch may display the resulting code. A team display swap moves the
   accepted code with the team's scores, timeouts, and serve marker.

An accepted duplicate-code change is one accepted match edit under Q26; its
warning and confirmation are part of that edit, not additional Undo steps.
They are not required again for ordinary scoring, timeout, or display-swap actions.
The duplicate permission never waives Q41's length/character validation. Show
the team being edited using its full name/display context, and both reviewed
codes. Commands still address stable identities rather than identical text.
Preserve the accepted pair on ordinary restart;
Q42 does not require the operator to reconfirm an already accepted code pair.

These policies do not select a new multi-field code editor or expand history
correction authority. Concurrent edits still need the eventual authorization,
freshness, and command-order checks. A stale remote draft must not overwrite
accepted newer data; exact review/revision contracts remain technical proposals.

## Written review cases

AV01–AV11 are written examples under confirmed Q41/Q42, not executable
tests or evidence of application, physical-device, or broadcast behavior.

| ID | Starting state / input | Action | Expected outcome under Q41/Q42 / noted technical proposal |
| --- | --- | --- | --- |
| AV01 | Opponent code PAN; candidates A, EAG, and A1234 | Save each candidate in a separate example | Accept one, three, and five permitted characters respectively; keep full names and team identities intact. |
| AV02 | Committed code OLD, opponent PAN; candidate ` eag ` | Review, then save | Show EAG before saving and commit EAG only on acceptance. The committed code stays OLD during the draft/review. |
| AV03 | Committed code EAG; candidates blank, only spaces, or ABCDEF | Attempt to save | Reject empty/over-limit input; keep EAG on air. Never silently shorten ABCDEF to ABCDE. |
| AV04 | Committed code EAG; candidates E AG, E-G, ÉAG, or an emoji | Attempt to save | Reject unsupported input under Q41; keep EAG. Do not transliterate É or remove punctuation to manufacture a valid code. |
| AV05 | Team A code OLD, opponent code PAN; candidate ` pan ` | Review, acknowledge the matching-code warning, and explicitly confirm | Show PAN before saving; accept PAN for team A only after confirmation. Keep both stable team identities, their independent full names and all match data. The changed code is one Undo-eligible match edit. |
| AV06 | Team A already EAG, opponent PAN; candidate `eag` | Review, then save the unchanged resulting code EAG | Do not report a collision with team A itself or create a new match edit/Undo opportunity. Keep existing match Undo unchanged. |
| AV07 | Live match; A's code changes from OLD to EAG as its latest accepted match edit; placement subsequently changes to bottom-center | Watch requests Undo, then a permitted phone/tablet performs Undo without any intervening match edit | Watch directs the operator to a phone and skips nothing. Phone restores OLD under Q26; scores/full names/team identities and current bottom-center placement remain intact. Undo is consumed. |
| AV08 | Codes EAG/PAN with asymmetric scores/timeouts and serve owned by EAG's stable team | Swap display order, then inspect the shared projection | Move each code with its own team values and serving marker; no duplicate error or identity reassignment arises from changing presentation order. |
| AV09 | AV05's uncommitted matching-code draft; an earlier point is still Undo-eligible | Cancel or decline the duplicate-code warning | Keep OLD/PAN, all match values, and the earlier match Undo opportunity. Opening the warning does not accept a code change. |
| AV10 | Both codes PAN were accepted after explicit confirmation; scores/timeouts differ and only one stable team serves | Award a point to one stable team, swap display order, and separately restart from the accepted match | Apply the point to the intended identity and move values with their owners on swap. Restore the accepted matching codes on restart without merging teams, resetting data, or requiring a new code-entry confirmation. |
| AV11 | A controller reviewed a warning for one team and code pair; relevant committed data changed before its confirmation arrived | Submit the obsolete duplicate-code confirmation | Proposed freshness rule rejects it without accepting different unreviewed values; refresh and review the current candidate/pair. Q42 requires deliberate confirmation; exact revision binding and receipt mechanisms remain technical work. |

The selected combination applies Q41's restricted uppercase rule and Q42's
explicitly confirmed duplicate permission together. It does not select strict
uniqueness or international-character entry. The existing 1–5-character limit,
no silent truncation, and stable identity rules remain fixed.

## Saved-code recovery: confirmed Q43

On 2026-10-21 the owner selected the recommended future recovery policy in the
question picker, which also offered an alternative and custom-answer field.
There is no existing application, saved-data migration, or import feature in
the project; this policy governs future recovery when such a state is encountered.

| Question | Recommended answer | Alternative | Owner answer |
| --- | --- | --- | --- |
| What if a future version restores a match containing EAGLES or ÉAG, outside the current code rules? | Preserve the match and original codes, keep scoring/corrections available, and require phone/tablet review and correction before starting a broadcast. Never silently shorten or replace them. | Allow broadcasting with the original codes after a phone/tablet warning and explicit acknowledgement, before correcting them. | Confirmed: recommended, PRD Q43. |

Q43 preserves scores, history, stable team identities, names, codes, serve,
timeouts, display order, placement, and the existing durably retained match Undo.
Restoration itself does not accept a code edit or clear match data. Show the
operator which codes need correction and keep the original values available
for review. Scoring and permitted corrections remain usable; ordinary scoring
still obeys the existing server-selection, cap-conflict, and potential-winner guards.

Prepare a replacement using Q41, showing any trimming/uppercase conversion
before saving. Apply Q42's matching-code warning and explicit confirmation where
needed. Canceling or rejecting the draft preserves the restored codes and match
Undo. Accepting a changed code is one Q26 match edit; Q27 keeps that code-edit
Undo on phones/tablets. Q40 preserves the current placement during match Undo.
No special recovery exemption from the existing Undo rules is selected.

Broadcast-start readiness checks the currently accepted codes. Enforce the
restriction on local, authorized Android, and watch start requests until the
required phone/tablet correction is complete. A warning acknowledgement alone
does not permit invalid codes to pass this gate. Once corrected, the normal
capture/provider/readiness conditions and deliberate start confirmation still
apply; correcting codes or restoring the app never starts publishing by itself.

RC01–RC08 are written review cases, not executed application or device tests.

| ID | Starting state | Action | Expected outcome under Q43 and existing confirmed rules |
| --- | --- | --- | --- |
| RC01 | Saved match has EAGLES/PAN, points 12–11, known serve/timeouts/history, and an existing match Undo | Restore the match | Keep all recorded values, including EAGLES and Undo. Mark the code for correction; keep permitted scoring/corrections available. Block broadcast start rather than rejecting or clearing the whole match. |
| RC02 | Saved code ` eag ` or ÉAG does not conform to Q41's final stored-code rule | Open phone/tablet correction | Keep the original committed value until acceptance. Show EAG as the reviewed replacement for ` eag `; require a valid operator-entered replacement for ÉAG rather than automatically transliterating it. |
| RC03 | RC01 restored, with an uncommitted EAG draft | Cancel or submit another invalid code | Preserve EAGLES/PAN, match data, and existing Undo. Broadcast start remains blocked. |
| RC04 | Restored team A code EAGLES; team B code PAN; operator chooses PAN for team A | Review and explicitly confirm Q42's matching-code warning | Commit PAN for team A as one match edit while keeping both identities separate. The legacy-code restriction is resolved when all accepted codes conform; normal stream readiness and confirmation still apply. |
| RC05 | RC01 is synchronized to an authorized watch or Android controller | Score within permitted controls; separately request stream start from each surface | Allow otherwise valid scoring and preserve data. Reject every start request with a direction to fix the codes on a phone/tablet; the watch cannot repair codes or bypass readiness. |
| RC06 | EAGLES was changed to EAG as the latest match edit; no broadcast has started | Undo that repair from a permitted phone/tablet | Restore EAGLES and consume Undo under Q26; retain current placement under Q40. Broadcast start is blocked again. Do not skip the code repair to undo an older point. |
| RC07 | Codes were corrected and are currently valid | Review and confirm stream start | Apply normal capture/provider readiness and deliberate confirmation. Code correction alone did not start publishing. A stale start cannot bypass the current accepted-state checks; exact revision/receipt handling remains technical work. |
| RC08 | Saved pair PAN/PAN was already valid and explicitly accepted under Q42 | Restore the match | Keep the accepted matching codes and separate team data. No Q43 invalid-character/length gate or new code-entry confirmation is introduced solely because the codes match. |

This settles the owner recovery policy. Schema/version detection, retention of
review/confirmation evidence, atomic recovery/Undo storage, controller errors,
and faithful rendering of original nonconforming text in recovery screens remain
technical design work. Q43 does not add a file import path, automatic publishing
resume, silent code migration, or an automatic stop to an already running stream.
Actual fit/failure handling for any preserved text still needs D10/M4.3 evidence.

## Handoff and remaining work

[M3.1](../subplans/m3-1-match-setup-and-presets.md) and
[M3.9](../subplans/m3-9-local-controls-and-scoreboard-projection.md) consume
the selected entry and display policy. [M6.3](../subplans/m6-3-android-controller-controls.md)
consumes equivalent Android-controller validation; [M4.3](../subplans/m4-3-encoded-scoreboard-composition.md)
and [V02](../subplans/m8-2-v02-received-overlay.md) still require actual fit and
received-readability evidence. [V10](../subplans/m8-10-v10-full-controller-matrix.md)
retains allowed/denied controller checks.

Q43 settles recovery of older/restored codes outside Q41's rule; technical
schema/storage/reviewed-state mechanisms still need their own disposition
before affected implementation. These policies do not approve startup truncation,
automatic replacement of existing codes, a new import feature, or recovery that
discards the committed match. Numeric overflow,
geometry, typography, contrast, and the eventual adaptive quality floor remain
separate D10 work. No application code, dependencies, tests, or prototype is added.

## Pre-answer documentation validation

On 2026-10-21 the existing planning-package checker passed with exit code 0
and no errors: 77 Markdown documents, 1,250 local links, 286 heading targets,
53 task records, 159 dependency edges, eight diagram pairs, and nine locked
skills. Separate reconciliation confirmed eight unique AV01–AV08 cases,
two pending answers, and unique sequential confirmed PRD rows Q1–Q40.
AGENTS.md and skills-lock.json retain their pre-work hashes.

Manual review checked the distinction between draft preparation and committed
code, no silent truncation, comparison against the opponent rather than the same
team, restricted watch Undo, current-position retention under Q40, and stable
team ownership after display swaps. These are written consistency checks;
neither candidate policy is confirmed and no application behavior is established.

## Post-answer synchronization and validation

On 2026-10-21 the owner selected the recommended restricted uppercase character
rule and the alternative permitting matching codes after a warning and explicit
confirmation. Q41/Q42 record that exact combination. AV05 was revised to accept
the explicitly confirmed duplicate; AV09–AV11 cover cancellation, identity-safe
matching codes through scoring/swap/restart, and a proposed stale-confirmation
guard. The PRD, M1.6, affected task handoffs, architecture, master plan, index,
and status dashboard are synchronized. Duplicate permission neither waives
character/length validation nor changes watch code-edit/Undo restrictions.

The existing documentation checker passed after synchronization: 77 Markdown
documents, 1,262 local links, 287 heading targets, 53 task records, 159 dependency
edges, eight diagram pairs, nine locked skills, exit code 0, and no errors.
Separate reconciliation confirmed unique sequential Q1–Q42 and AV01–AV11,
one confirmed recommended character answer, one confirmed alternative duplicate
answer, nine confirmed D10 owner choices, and 13 still-open aggregate decision
records. Task counts remain 7 complete / 1 in progress / 45 not started /
0 unverified; milestone totals reconcile to 53. Entry/evidence sections are
present, completed checklists have no unchecked criteria, and not-started
checklists remain unchecked. AGENTS.md and skills-lock.json hashes are unchanged.

These checks establish documentation consistency, not delivered behavior.
Older/restored nonconforming codes, technical preference/confirmation contracts,
numeric fit/overflow, geometry/type/contrast, and actual encoded/received
readability remain D10 work. G1/G2 and future public-boundary confirmation
remain required; no application code, dependencies, tests, prototype, or
device/provider action was added.

## Q43 synchronization and validation

The owner selected the recommended saved-code recovery policy on 2026-10-21.
Q43 and RC01–RC08 now preserve the original codes and match, keep permitted
scoring/corrections usable, and require phone/tablet review and correction
before broadcast start. Local, Android, and watch start controls share that
readiness restriction. Repair cancellation preserves the restored data/Undo;
accepted repair follows ordinary Q26/Q27 match Undo, retaining current placement
under Q40. Recovery or successful correction never starts publishing by itself.

The PRD, M1.6, affected restore/setup/publishing/controller/acceptance task
handoffs, architecture, master plan, index, and dashboard are synchronized.
The existing planning-package checker passed: 77 Markdown documents, 1,267 local
links, 292 heading targets, 53 task records, 159 dependency edges, eight diagram
pairs, nine locked skills, exit code 0, and no errors. Separate reconciliation
confirmed unique sequential Q1–Q43, eight unique RC01–RC08 cases, eleven
AV01–AV11 cases, one confirmed recovery answer, ten confirmed D10 owner choices,
and 13 still-open aggregate decision records. Task counts remain 7 complete /
1 in progress / 45 not started / 0 unverified, with milestone totals reconciled
to 53 and no contradictory or missing task checklist/entry/evidence sections.
AGENTS.md and skills-lock.json hashes are unchanged.

These are documentation results. Recovery/schema/reviewed-state mechanisms,
numeric fit/overflow, visual limits, and real encoded/received readability
remain open. G1/G2 and future public-boundary confirmation still apply; no
application code, tests, dependencies, prototype, or device/provider action
was added. The next recommended review is numeric fit/overflow and visual limits.

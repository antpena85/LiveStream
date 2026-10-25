# M1.4 venue upload observations and evidence gaps

Recorded: 2026-10-18 (America/New_York)  
Status: Complete for M1.4 planning; owner-selected mobile-data default and explicit measurement gaps recorded. No uploads measured.  
Task: [M1.4](../subplans/m1-4-venue-upload-baseline.md)  
Authority: [PRD FR-01](../../PRD.md#fr-01-livestream-to-an-external-platform) and [PRD 5.4](../../PRD.md#54-details-to-confirm-before-implementation)  
Open decisions: [D01 and D07](../MASTER_PLAN.md#7-open-decision-register)

## Known facts and provenance

- PRD Q14 confirms that both Wi-Fi and mobile data are available. No raw upload
  measurements have been supplied in the retained project records.
- Asked about venue upload measurements on 2026-10-18, the owner answered:
  **“I do not have venue specific, this is due to venue always changing.
  Default to mobile data”.** Mobile data is the confirmed default broadcast
  connection; Wi-Fi remains a supported alternative. There is no fixed venue
  baseline to record.
- The [M1.2 inventory](m1-2-device-roles-and-os-inventory.md) records five available
  phones, interchangeable roles, and the main test group of one Pixel 10 Pro,
  its watch, and the Pixel 8 Pro. Installed OS versions/builds remain unknown.
- The approved stable-connection validation baseline remains **720p/30 fps**.
  This is a future broadcast-validation target, not an observed network result
  or a minimum quality setting for adaptation.
- A documentation review on 2026-10-18 found no venue nickname, measurement
  sessions, raw upload results, measurement tool, or observed handoff constraints.
  The owner's answer explains the missing venue-specific measurements.
  Completion uses the task's existing allowance for explicit evidence gaps;
  measurements can be added per session as venues change.

No phone, venue network, or broadcast was tested for this record. Availability
does not establish throughput, stability, or support for a particular quality.

## Connection inventory

These rows record missing evidence, not measurement sessions. Unknown values
must not be entered as zero throughput or a failed test.

| Case | Connection and intended use | Confirmed availability | Upload observations | Outstanding evidence |
| --- | --- | --- | --- | --- |
| N01 | Wi-Fi; supported alternative | Available, PRD Q14; access at each future venue unverified | Unknown; owner has no venue-specific measurements | Session venue nickname and phone position, date/time/timezone, physical phone and OS/build, measurement tool/method, individual upload results and units, variability, and observed restrictions. |
| N02 | Mobile data; owner-selected default | Available, PRD Q14; coverage/capacity at each future venue unverified | Unknown; owner has no venue-specific measurements | Session venue nickname and phone position, date/time/timezone, physical phone and OS/build, active mobile connection, measurement tool/method, individual upload results and units, variability, and observed restrictions. |

Wi-Fi/mobile handoff behavior and restrictions are **unknown**. Availability of
both connections does not show that a broadcast or remote-control session will
survive a change between them.

The mobile-data default records intended product behavior. Android routing
mechanisms, behavior when Wi-Fi is also connected, and automatic handoff/fallback
remain M2.6/D07 design work. No routing API, forced network change, seamless
handoff, or particular mobile service/coverage is selected by this inventory.

## Proposed collection method

This is a suggested way to fill the gaps, not an owner-approved procedure or a
new product acceptance requirement. Existing partial results can be recorded
with their limitations instead of discarded.

1. At a representative session's intended streaming position, use a venue
   nickname and identify the physical Pixel phone by its inventory label.
   Record its installed OS/build
   if known, otherwise explicitly retain those fields as unknown. The main
   test group's Pro or Pixel 8 Pro is a suitable planning choice; this does not
   assign a permanent streamer or establish support for other phones.
2. Prioritize the default mobile-data case N02 on that phone, then measure N01
   separately when venue Wi-Fi is available. Keep unrun cases as explicit gaps
   and add separate sessions as venues change. Record which connection
   actually carried each test. For the mobile run, turn Wi-Fi off and record
   the displayed mobile connection when known. A workstation measurement is
   not a substitute for a venue/phone observation.
3. Record the measurement tool and version if available, chosen test endpoint
   if shown, test duration if known, and each upload result in Mbps. For an
   initial view of variability, consider three separately timed runs per
   connection and a later sample during representative venue activity. This
   sample count is a proposal, not a pass threshold.
4. Retain individual results with timestamps and conditions such as approximate
   venue activity, phone position, and any observed interruptions. For multiple
   comparable samples, report their observed range and sample count; identify
   changed configurations separately. Keep failed attempts separate from
   checks that were never run. Do not infer sustained broadcast capacity or
   select an encoder bitrate from a single speed-test result.
5. Record any observed sign-in/access restrictions or interruptions. If handoff,
   restricted uploads, or outages have not been exercised, leave them untested.
   Their future broadcast scenarios and recovery policy belong to M2.6/V08.

Use nicknames and sanitized results. Do not retain network passwords, public
IP addresses, precise personal locations, account details, or stream credentials.
No particular measurement service, application dependency, or purchase is selected.

## Fields for a future measurement session

No sample session is represented by the following template.

| Field | Record when a session is supplied |
| --- | --- |
| Session and case | Unique session label; N01 or N02 |
| Venue and position | Venue nickname and approximate streaming position |
| Date/time | Actual measurement date, local time, and timezone |
| Phone and software | Physical inventory label, role for this session, installed Android version/build or explicit unknowns |
| Active connection | Wi-Fi or mobile; displayed connection type if known; any VPN/tethering or other configuration relevant to the result |
| Method | Tool/version, endpoint if shown, test duration if known, and how the active connection was checked |
| Upload samples | Individual upload Mbps values with their timestamps; record reported units before any conversion |
| Variability and context | Sample count, observed range when comparable, venue activity, signal/position changes, and interruptions |
| Constraints | Observed access/sign-in restrictions; handoff observed or untested; unavailable or failed checks and their reasons |
| Provenance | Owner-reported or directly observed; sanitized evidence reference if available |

## Handoff to quality-policy and broadcast validation

| Stage | Use of N01/N02 | Evidence still required |
| --- | --- | --- |
| [M2.6 — Adaptation and reconnection policy](../subplans/m2-6-adaptive-quality-policy.md) | Plan around the confirmed mobile-data default, preserve Wi-Fi support, and use observations from changing venues together with M2.2 media evidence. Keep missing measurements explicit. | Measured inputs and rationale for D07 bitrate bounds, observation windows, downgrade/recovery behavior, reconnect budgets, routing of the default when Wi-Fi is connected, and handoff policy. |
| [V01 / M8.1 — Sustained broadcasting](../subplans/m8-1-v01-sustained-broadcasts.md) | Validate the approved 720p/30 fps baseline on an actually stable connection. | Real destination sessions and sustained-broadcast evidence; speed-test samples do not pass V01. |
| [V08 / M8.8 — Network and lifecycle stress](../subplans/m8-8-v08-network-and-lifecycle-stress.md) | Exercise controlled restricted upload on both connection types and the eventual handoff/recovery policy. | Received broadcast and preserved match/overlay state under the selected D07 criteria. |

Minimum/maximum bitrate, quality floor, resolution/frame-rate changes, response
thresholds/timing, recovery hysteresis, reconnect limits, and handoff guarantees
remain undecided. No quality parameter is confirmed by this inventory.

## Completion assessment

All three M1.4 planning criteria have documentation evidence:

- N01 and N02 separately record confirmed connection availability and explicit
  gaps for missing session configuration, upload samples, and variability.
  The owner's changing-venue explanation and mobile-data default are retained.
- The approved stable-connection validation baseline remains 720p/30 fps.
- No bitrate, threshold, timing, or handoff parameter is presented as confirmed.

M1.4 is complete on the checklist's **explicit evidence gaps** path. No network
measurement was collected. This completes the documentation prerequisite for
M2.6, whose M2.2 dependency, entry gates, and need for measured inputs still
apply. Drafting scenarios can proceed without inventing numeric policy values.
D01/D07 remain open; their required evidence must be resolved before G2 passes.

When observations arrive, add dated sessions with their source and actual
configuration, update N01/N02 separately, and carry any remaining gaps forward.
Neither a documentation gap disposition nor a short upload test passes the
later sustained-broadcast or restricted-upload acceptance checks.

G1/G2 and the later validation gates retain their existing entry conditions.
This inventory does not implement adaptation or establish application behavior.

## Documentation validation

Validated on 2026-10-18 with `node plan/tools/check-planning-package.cjs`:
**PASS**, exit code 0, no reported errors. The checker covered 65 Markdown
documents, 992 local link occurrences, 224 heading targets, 53 tasks,
159 dependency edges, eight diagram pairs, and nine locked skills.

A separate reconciliation of the task records and Markdown dashboard passed:
5/53 tasks complete, M1 at 4/6, 48 tasks not started, and 13 decision records
still open. All completed tasks have checked acceptance lists and retained
evidence. These checks validate documentation consistency, not network capacity
or application behavior.

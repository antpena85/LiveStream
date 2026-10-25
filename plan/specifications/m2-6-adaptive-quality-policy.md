# M2.6 adaptive quality and recovery proposal

Recorded: 2026-10-23 (America/New_York)  
Status: Measurable experiment design prepared; operating parameters not approved or measured.  
Task: [M2.6](../subplans/m2-6-adaptive-quality-policy.md)

## Confirmed inputs and limits

Mobile data is the default; Wi-Fi remains supported. The stable validation
baseline is 720p/30. [M1.4](../inventories/m1-4-venue-upload-baseline.md) records
no venue upload samples. M2.2 supplies no physical encoder/queue/thermal results
yet. Do not turn missing measurements into throughput estimates or approved
bitrate limits.

Recommend bitrate-first adaptation with hysteresis and explicit bounded
reconnection, initially holding landscape geometry/fps constant. This reduces
pipeline reconfiguration during the first experiment; resolution/fps changes
remain candidates until demonstrated. Network transport type is not measured
available upload capacity. Use actual send-queue/congestion/media feedback and
observe the received scoreboard. YouTube recommends representative motion/audio
tests and monitoring stream health, not relying on a speed test alone.
[YouTube encoder guidance](https://support.google.com/youtube/answer/2853702?hl=en).

## Laboratory starting hypotheses

These numbers make proposed experiments repeatable. They are **not product
bounds, user-approved settings, upload observations, or evidence of legibility**.
Replace them with measured values and provider-compatible settings before G2.

| Parameter | Initial experiment hypothesis | Decision evidence |
| --- | --- | --- |
| Output | AVC/AAC, 1280×720, 30 fps, 2-second keyframe interval | Candidate encoder and each provider accept it; all required fields readable. |
| Video starting/floor/ceiling | 3,000 / 1,500 / 4,500 kbit/s | Measured upload, encoder response and received motion/scoreboard quality; reject an unreadable floor. |
| Observation | Sample actual send-queue delay and outbound telemetry every 1 second | Verify telemetry meaning, clock and relation to congestion. |
| Reduction | Three successive samples over 500 ms queue delay: request 20% lower bitrate, at most once per 5 seconds | Restriction-step runs show bounded queue and preserved match; avoid oscillation. |
| Recovery | 30 seconds healthy below 150 ms queue delay: request 10% increase, at most once per 15 seconds | Stable recovery without renewed queue growth or lost overlay. |
| Reconnect | Trial delays 1, 2, 4, 8, 15 seconds; stop after 60 seconds elapsed outage | Provider/backoff behavior, cancellation and visible error; no unbounded retry. |

The proposed ladder is an engineering test input, not a provider recommendation.
Choose provider-specific settings when the observed service constraints differ.
Do not implement these thresholds as final policy without the recorded decision.

## Routing, stops and validation

Propose explicit publishing-socket selection for the preferred mobile network,
while allowing controller sockets to use their LAN. When cellular is unavailable,
propose a visible operator choice for Wi-Fi; silent fallback/data-spend policy
is unresolved. A disconnected Android Network cannot keep its old sockets;
reacquire the route and reconnect under the eventually selected policy.
[Android connectivity state](https://developer.android.com/develop/connectivity/network-ops/reading-network-state).

Run AQ01 stable baseline; AQ02 stepped upload restriction; AQ03 sustained floor;
AQ04 recovery; AQ05 total outage/retry exhaustion; AQ06 user Stop during retry;
AQ07 Wi-Fi/cellular loss and handoff; AQ08 full-field readability at every
selected mode. Repeat Wi-Fi and cellular cases separately. Record actual route,
imposed/observed capacity, queue/requested/observed bitrate, timing, drop/stop
state, match revision and received-field correctness. Viewer playback delay is
separate from command-to-overlay response.

Q44 keeps complete numbers and reviewed readable type. Q45 stops publishing
with notice after a diagnosed numeric-fit failure while preserving the full
new match/off-air scoring. Compression-related unreadability or insufficient
bandwidth without numeric fit failure still needs a D07 recovery decision.
No known-unreadable mode should be selected as a normal supported downgrade.

All AQ cases remain unrun. D07/D08 and M2.6 stay open for measurements, chosen
parameters, routing/fallback decisions and media evidence.

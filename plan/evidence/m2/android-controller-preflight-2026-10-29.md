# Android controller/uplink preflight — 2026-10-29

Scope: M2.4 physical prerequisites only. No Android-phone LAN controller harness
or publishing test was executed. See the [topology specification](../../specifications/m2-4-remote-topology-feasibility.md).

## Owner clarification and observed targets

Later owner clarification: the phone labeled Android 16 is in the middle of
an update. Pause further device-dependent checks on that unit until the owner
reports completion and ADB re-verifies the installed OS/build. The destination
version/build and completion time are unknown. Values and ping results below
are historical observations of CP1A.260505.005, not a post-update baseline;
the update may also affect interpretation of the short latency sample, but no
causal explanation for its variability is established. Do not interrupt the
update, reboot the phone or infer completion from ADB availability.

The owner confirmed both connected Pixel 8 Pro phones are theirs and available,
and selected Android 16 first, then Android 17 separately. This adds a second
Pixel 8 Pro to the earlier single-unit inventory; it does not replace an existing
unit or change interchangeable phone roles. All listed devices were authorized
in ADB. These non-identifying labels distinguish observed targets by build;
device serials and wireless debugging addresses are deliberately omitted.

| Session role | Model | Installed Android / API | Build | Security patch |
| --- | --- | --- | --- | --- |
| Proposed streamer | Pixel 10 Pro XL | 17 / 37 | CP3A.261005.005 | Not re-read in this preflight |
| First controller target | Pixel 8 Pro | 16 / 36 | CP1A.260505.005 | 2026-05-05 |
| Later controller target | Pixel 8 Pro | 17 / 37 | DP11.261002.005 | 2026-10-05 |

Installed values come from read-only ADB `getprop` queries. Build names do not
establish stable/beta enrollment or conformance to the owner's stable/N−1
expectation. No update, reset, deployment or role transfer was performed.

## Read-only network baseline

ADB `ip route show table all` was processed in memory, retaining only interface
names and whether any default route existed. IP addresses, network names and
route tables were not saved. Global Wi-Fi/mobile-data toggle values were `1`
on all three phones; toggles do not demonstrate usable service or actual routing.

| Target | Interfaces appearing in route tables | Any default route |
| --- | --- | --- |
| Pro XL | rmnet1, wlan0, ipsec206, dummy0, lo | Yes |
| Android 16 Pixel 8 Pro | wlan0, dummy0, lo | Yes |
| Android 17 Pixel 8 Pro | wlan0, dummy0, lo | Yes |

These observations do not identify the app-selected internet network, carrier
service, shared LAN, client isolation, hotspot availability, VPN configuration,
upload bandwidth, reachability or simultaneous cellular publishing. An interface
name alone does not establish its purpose or whether it affects a future test.
No packet was sent to a publishing destination or external test endpoint.

## Remaining setup

Confirm the owner's available network arrangement before setting up a controller
experiment. Compare a shared Wi-Fi LAN and a Pro XL hotspot separately; keep the
Android 16 and Android 17 runs distinct. Do not assume an RTMP publisher supports
network-bound sockets from generic Android routing capability. A future isolated
app-level harness must exercise permissions, authenticated peer exchange and
actual selected-network observations; shell routing metadata is only preflight.

M2.4, D05/D07 and G2 remain open. No task acceptance was newly checked.

## Shared-Wi-Fi shell reachability observations

The owner confirmed shared Wi-Fi is available for the phones and the Pro XL
has working mobile data. This is owner-reported service availability, not an
observed app-selected cellular publishing route. No network settings changed.

Tested the Android 16 controller first, then Android 17 separately. Each phone
sent three ICMP echo requests through `wlan0` to its peer's current Wi-Fi IPv4
address. Addresses and raw command output were processed in memory, not retained.
Both directions completed with exit 0, three transmitted/three received and 0%
loss in each short sample.

| Controller build | Direction | Minimum / average / maximum RTT (ms) | Retained report |
| --- | --- | --- | --- |
| CP1A.260505.005 (Android 16) | Controller → Pro XL | 128.801 / 445.293 / 1033.686 | [Android 16](android-controller-lan-preflight-20261030T013439Z.json) |
| CP1A.260505.005 (Android 16) | Pro XL → controller | 146.552 / 429.154 / 630.526 | Same report |
| DP11.261002.005 (Android 17) | Controller → Pro XL | 41.963 / 189.414 / 310.755 | [Android 17](android-controller-lan-preflight-20261030T013518Z.json) |
| DP11.261002.005 (Android 17) | Pro XL → controller | 60.185 / 230.405 / 407.632 | Same report |

The UTC report dates are October 30; local observation date is October 29 in
America/New_York. These are shell-level reachability samples, not an app sandbox
permission test, authenticated transport, discovery test, sustained reliability
measurement or a controlled OS-performance comparison. No app/publisher was
installed or started. A maximum sample above one second is retained, not
discarded; no PRD controller-latency acceptance is inferred from these pings.

Next define an isolated app-level LAN harness and its public T04 behavior before
implementation: explicit target pairing, permission-denial outcomes, bounded
authenticated request/reply and reconnect evidence. Preserve Wi-Fi controller
traffic while independently observing a requested cellular network; do not bind
the entire process or call shell ping proof of RTMP-library compatibility.
Actual publishing, mobile upload/adaptation and provider evidence remain separate.
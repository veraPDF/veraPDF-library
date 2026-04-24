# Security Policy

This security policy applies to all software products maintained by the
[Open Preservation Foundation](https://openpreservation.org) (OPF), including
veraPDF, JHOVE, Open Fixity, and associated libraries and tools.

## Reporting a Vulnerability

**Please do not report security vulnerabilities as public GitHub issues.**
Public disclosure before a fix is available puts users at risk.

Report vulnerabilities using one of these private channels:

- **Email:** security@openpreservation.org
- **GitHub:** Use the [Report a vulnerability](https://docs.github.com/en/code-security/security-advisories/guidance-on-reporting-and-writing/privately-reporting-a-security-vulnerability) button on the Security tab of the relevant repository

Include as much of the following as you can:

- A description of the vulnerability and its potential impact
- The affected product(s) and version(s)
- Steps to reproduce or proof-of-concept code
- Any suggested mitigations you are aware of

## Our Commitment

When you report a vulnerability to us we will:

- **Acknowledge** your report within 5 business days
- **Assess** the severity and scope and keep you informed of our findings
- **Coordinate** the fix and disclosure timeline with you
- **Credit** you in the security advisory, unless you prefer to remain anonymous
- **Notify** you before we publish any advisory

We will not take legal action against researchers who report vulnerabilities in
good faith and act in accordance with this policy.

## Disclosure Timeline

We follow a coordinated vulnerability disclosure (CVD) process:

1. **Day 0** — You report the vulnerability privately
2. **Within 5 business days** — We acknowledge receipt
3. **Within 14 days** — We provide an initial assessment, including severity and
   whether we can reproduce the issue
4. **Ongoing** — We work on a fix and agree a disclosure date with you

The disclosure timeline depends on severity:

| Severity | Target fix and disclosure |
|----------|--------------------------|
| Critical / actively exploited | As fast as possible — we may disclose early with mitigations if a fix is not yet ready |
| High | Within 30 days |
| Medium | Within 60 days |
| Low | Within 90 days |

We will always discuss timeline with you before publishing. If circumstances
require us to deviate from these targets we will explain why and agree an
alternative with you.

## Out of Scope

The following are outside the scope of this policy:

- Denial of service attacks
- Social engineering or phishing of OPF staff
- Physical security issues
- Vulnerabilities in third-party dependencies — please report these upstream; we will still triage and advise if you are unsure

## Public Disclosure

Once a fix is available we will:

1. Publish a **GitHub Security Advisory** in the affected repository
2. Post a summary on the **[OPF website](https://openpreservation.org)**
3. Issue a **new release** of the affected product with the fix included

We will not publish details of a vulnerability before a fix is available,
except in cases where active exploitation means users must be warned
immediately.

## Regulatory Obligations

As part of our compliance with the EU Cyber Resilience Act (CRA), OPF reports
actively exploited vulnerabilities to ENISA within 24 hours of becoming aware
of them, and submits full notifications within 14 days. This regulatory
reporting is separate from and does not replace our commitment to coordinating
disclosure with you.

---

_Policy version: 1.0 — April 2026_  
_Review due: April 2027_

# DIGITAL MIND SOURCE-AVAILABLE LICENSE

**Instrument:** Digital Mind Source-Available License (**“DMSAL”**)  
**Version:** 1.0  
**Effective date:** 1 January 2020  
**Governing law:** Romania  

---

## How this Licence works (guide — non-operative)

This guide is for reading convenience only. It has no legal effect. The Preamble and Articles 1–15 are operative.

| Actor | Rights |
|-------|--------|
| Public reader | **Inspection** of Source Code (Article 4). No Use. |
| Contributor | May embed the Software in **Client Software** and make a **Qualifying Delivery**. |
| End Beneficiary (after Qualifying Delivery) | **Pass-through sublicence to Use** only the versions **listed** in the Delivery Record, as embedded in that Client Software (Articles 5–6). |
| Licensing Party | The Full Owner that signs the commercial deal or ships the Qualifying Delivery; alone owes **Contracted Related Services**. |
| Broader exploitation | Standalone product, OEM, Software-Primary, or handing Client Software to another vendor → **Article 9**. |

---

## Preamble

(A) **DIGITAL SOLUTIONS EXPERT S.R.L.** (**“DSE”**) and **MIND STUDIO S.R.L.** (**“MS”**) (each a **Full Owner**, together the **Full Owners**) author, maintain, and publish software under the common commercial designation **“Digital Mind”**.

(B) **Digital Mind** is a shared brand only: not a legal person, not a copyright owner, and not a required contracting party.

(C) Copyright in each Contribution remains with the Full Owner that validly holds it. The Full Owners grant each other **Independent Commercialization Authority**: either may licence or deliver the Digital Mind–branded corpus to third parties **alone**, as sole **Licensing Party**, without co-signature of the other. That authority is ownership of Contributions plus reciprocal commercialization and sublicensing grants under Romanian copyright law (Article 3).

(D) Public Source Code is for **inspection only**. This is not an OSI-approved open-source licence. **Use** requires Article 5, 6, or 9.

(E) Only the **Licensing Party** owes the customer the **Contracted Related Services**. The other Full Owner owes no services to that customer unless it contracts separately.

(F) Upon a **Qualifying Delivery**, the Licensing Party grants the End Beneficiary a **pass-through sublicence of Use rights** for the **exact Delivered Versions** listed in the Delivery Record. That grant is a Use sublicence, not a copyright transfer, and does not itself require a separate Article 9 instrument (subject to Articles 5 and 6).

(G) This instrument is the public copyright and licensing framework. Fees, SLAs, and commercial particulars appear only in a Separate Commercial License or client engagement documents that incorporate or reference this Licence.

**Operative effect.** By publishing or contributing Software under this Licence, each Full Owner makes the grants and acknowledgements herein. A Recipient’s rights arise only as expressly set out in the Articles below.

---

## Article 1 — Definitions

**“Affiliate”** means an entity that directly or indirectly Controls, is Controlled by, or is under common Control with an End Beneficiary. **“Control”** means more than fifty percent (50%) of the voting interests.

**“Business Day”** means a day other than Saturday, Sunday, or a public holiday in Romania.

**“Client Software”** means an application, system, integration, or other work product that:

1. is created or substantially configured **on a client’s order** under a Contributor’s engagement with that client;  
2. incorporates, links, bundles, or otherwise requires the Software to execute; and  
3. is not Software-Primary (Article 5.9).

**“Contribution”** means Source Code, Object Code, documentation, or other protectable material authored by or for a Full Owner and incorporated into the Software.

**“Contributor”** means only:

1. a **Full Owner**; or  
2. an employee, contractor, or subcontractor that holds an **express written mandate** from a Full Owner (employment contract, MSA, SOW, or power of attorney) that **expressly** covers creation, build, or delivery of Client Software embedding the Software, and that acts within that mandate.

Implied authority alone is insufficient. Persons without status (1) or (2)—including public repository participants, forks, and third-party integrators—obtain no Article 5 rights.

**“Contracted Related Services”** means Related Services that are **expressly included** in at least one of:

1. the Separate Commercial License;  
2. the statement of work, order, or support schedule for the Client Software; or  
3. a written support or warranty annex,

including security remediation required by Article 6.4 during an active paid support or warranty period. Services not so included are not owed under this Licence.

**“Deliverable”** means Client Software supplied by a Contributor under a Qualifying Delivery. (Where a client contract uses “deliverable” more broadly, only the Client Software portion that embeds the Software is a Deliverable under this Licence.)

**“Delivered Version”** means a release of the Software that is **listed in the Delivery Record** with:

1. a version number; and  
2. at least one of: commit ID, build ID, Maven/Gradle coordinate with version, or cryptographic checksum.

Presence on the shipped medium without listing in the Delivery Record does **not** make a release a Delivered Version.

**“Delivery Record”** means handover documentation that includes all of the following:

1. a list of each Delivered Version as required above;  
2. a **minimum SBOM** (or equivalent dependency lock / manifest excerpt) identifying the DMSAL components embedded; and  
3. this Licence, a URL to it, or the NOTICE in Schedule B (or an equivalent DMSAL v1.0 citation).

The Licensing Party shall issue a Delivery Record for **every** Qualifying Delivery, before or upon handover.

**“Derivative Work”** means a work based on the Software within the meaning of applicable copyright law.

**“Digital Mind”** means the common brand under which the Full Owners publish the Software. Branding only; not a legal entity; not a copyright owner.

**“DMSAL”** / **“this Licence”** means this Digital Mind Source-Available License, version 1.0, including its Schedules.

**“End Beneficiary”** means the client (or expressly named recipient) that lawfully receives Client Software containing the Software under a Qualifying Delivery.

**“Full Owner”** means each of:

1. **DIGITAL SOLUTIONS EXPERT S.R.L.** (**“DSE”**), CUI RO38125095; contact person: Lucian Laurențiu Dragomir; and  

2. **MIND STUDIO S.R.L.** (**“MS”**), CUI RO37308884; contact person: Răzvan-Gabriel Ionescu.

**“Independent Commercialization Authority”** means the irrevocable right of either Full Owner, under Article 3, to reproduce, adapt, distribute, communicate to the public, Use, and sublicence the Software (including the other Full Owner’s Contributions as licensed under Article 3) to third parties **alone**, as Licensing Party, with authority binding toward that third party for the scope granted.

**“Licensing Party”** means the Full Owner that:

1. executes a Separate Commercial License; or  
2. is the Full Owner that ships a Qualifying Delivery, or under whose express written mandate a Contributor ships that Qualifying Delivery.

**“Object Code”** means machine-executable or intermediate compiled form of the Software.

**“Permitted Processor”** means a host, cloud operator, or IT contractor that runs Client Software solely for an End Beneficiary or its Affiliate under confidentiality, without an independent right to exploit the Software.

**“Qualifying Delivery”** means a delivery of **Client Software** by a Contributor that meets **all** of the following:

1. the shipper is a Full Owner, or is a Contributor acting under that Full Owner’s express written mandate;  
2. the delivery is accompanied by a Delivery Record that conforms to this Article 1; and  
3. the shipment is not Software-Primary (Article 5.9).

**“Recipient”** means any person that accesses or possesses the Software.

**“Related Services”** means implementation, integration, configuration, training, support, maintenance, updates, warranty remediation, and SLA-type professional services connected with a licence or Client Software.

**“Separate Commercial License”** means a written agreement compliant with Article 9, granting rights beyond Articles 4 to 6.

**“Software”** means Source Code, Object Code, libraries, modules, packages, APIs, schemas, documentation, build scripts, and sample configurations published or distributed **under this Licence text** (or an unmistakable reference to it) and under the Digital Mind brand, excluding Third-Party Components.

**“Software-Primary”** has the meaning given in Article 5.9.

**“Source Code”** means the human-readable form of the Software preferred for modification.

**“Third-Party Components”** means third-party materials under their own licences; those licences continue to apply.

**“Use”** means to execute, load, invoke, import, link, run, or operate the Software in a runtime, build, test, staging, or production environment for a purpose other than mere inspection under Article 4.

- Automated indexing or syntax highlighting incidental to viewing Source Code is not Use.  
- Compiling or running the Software (including in CI) for development, test, or production of a system is Use.

---

## Article 2 — Temporal scope; adoption of this text

2.1 **Effective date.** Version 1.0 is effective as of **1 January 2020** for Software distributed or republished under this Licence text (or a clear reference to DMSAL v1.0).

2.2 **Copies that include this text.** Any copy of the Software that contains this Licence (or points to it as the governing licence) is governed by these Articles from the date of that distribution or republication.

2.3 **Prior distributions without this text.** Where Software was delivered before this text was attached or referenced, the terms of that delivery (and any then-applicable client contract) govern that delivery, until the parties expressly adopt DMSAL by written reference (including a Delivery Record that cites DMSAL v1.0) or the Software is republished under this text.

2.4 **No constructive antedating.** Article 2.1 does not rewrite historic contracts that never incorporated DMSAL. It fixes the licence terms for distributions made under this instrument.

---

## Article 3 — Copyright, mutual commercialization grant, services

### 3.1 Copyright in Contributions

(a) Subject to written assignment or employment/contractor vesting, each Full Owner owns the copyright in Contributions it introduces.  
(b) Jointly authored Contributions are jointly owned to the extent provided by law, without prejudice to Article 3.2.

### 3.2 Independent Commercialization Authority (binding inter se)

Each Full Owner, as copyright owner (or joint owner) of its Contributions, hereby grants the other Full Owner an irrevocable (subject to Article 13.2), perpetual for the copyright term, worldwide, royalty-free:

(a) licence under all copyright and related rights in those Contributions to reproduce, adapt, translate, distribute, communicate, Use, and create Derivative Works; and  
(b) right to sublicence the same to third parties,

in each case solely as needed to exercise Independent Commercialization Authority over the Software as a whole (including combining with the grantee’s own Contributions), so that either Full Owner may act as sole Licensing Party toward a customer without the other Full Owner’s co-signature.

Internal revenue sharing, if any, is outside this Licence and does not affect the validity of customer-facing grants.

### 3.3 Effect toward third parties

A licence, Qualifying Delivery, or pass-through sublicence under Articles 5, 6, or 9 issued by one Licensing Party is valid and binding toward the licensee or End Beneficiary as a grant from a party with Independent Commercialization Authority. The non-licensing Full Owner shall not assert copyright against that licensee or End Beneficiary for exercise within that grant’s scope (including Use of Delivered Versions under Article 5.3).

### 3.4 Ownership model

The model under this Licence is ownership of Contributions plus mutual commercialization and sublicensing grants, which together yield Independent Commercialization Authority. This Licence does not create two mutually exclusive absolute titles to one and the same copyright object.

### 3.5 Vesting

Each Full Owner shall ensure Contributions it introduces are vested in it by written instrument with employees and contractors before incorporation into a released artefact. A defect as to a Contribution is limited to that Contribution (Article 3.8).

### 3.6 Exclusivity toward third parties

Exclusive licences to third parties require prior written consent of the other Full Owner (signed or confirmed in writing by an authorised representative, including email). Absent such consent, Article 9 licences are non-exclusive.

### 3.7 External contributors

No rights accrue to external contributors without a written CLA or assignment bringing the material under Articles 3.1–3.2. Unauthorised contributions are rejected.

### 3.8 Warranty of authority; Contributor mandate; limited indemnity

(a) **Authority.** Each Full Owner warrants that, when acting as Licensing Party, it has Independent Commercialization Authority under Article 3.2 sufficient to grant the rights in Articles 5, 6, or 9.

(b) **Contributor mandate.** The Licensing Party warrants to the End Beneficiary that any Qualifying Delivery was made by a Contributor as defined in Article 1 (Full Owner, or person under a valid express written Full Owner mandate).

(c) **Exclusive remedies for (a)–(b).** Cure (repair, replacement, re-procurement of rights, or ratification of mandate) or termination of the affected grant for the affected portion, at the Licensing Party’s election.

(d) **IP indemnity (separate cap).** For third-party copyright claims alleging that the Software as delivered under a Qualifying Delivery or Article 9 licence infringes a third party’s copyright, the Licensing Party shall indemnify the End Beneficiary or licensee for documented defence costs and final awards, up to the greater of:

1. two times (2×) the fees paid to that Licensing Party for the relevant Client Software or Separate Commercial License in the twelve (12) months before the claim; or  
2. EUR 25,000 (twenty-five thousand euros),

unless a Separate Commercial License sets a higher cap. This indemnity cap is separate from, and is not reduced by, the general liability cap in Article 12.2(b).

(e) Broader remedies require express written agreement.

(f) **Confirmation letter.** On reasonable written request from an End Beneficiary or Article 9 licensee, the Licensing Party shall, within fifteen (15) Business Days:

1. issue a short confirmation letter stating that it acts under Independent Commercialization Authority and, where applicable, that the delivery was a Qualifying Delivery; and  
2. where the request reasonably relates to Contributions of the other Full Owner, use commercially reasonable efforts to obtain a countersignature or short co-confirmation from that other Full Owner within the same period, or provide a redacted extract of any inter-company acknowledgement that exists.

### 3.9 Contracted Related Services

(a) The Licensing Party alone shall perform or procure the Contracted Related Services for that customer.  
(b) The other Full Owner has no services obligation to that customer unless it signs a separate services agreement or is named as subcontractor.  
(c) The Licensing Party may subcontract but remains responsible for Contracted Related Services.  
(d) Digital Mind branding does not create joint services liability.  
(e) This Licence does not create an open-ended duty to provide Related Services that were never contracted.

### 3.10 Brand; no partnership

Digital Mind is branding only (Article 8). No partnership, joint venture, or agency arises beyond Articles 3.2–3.3.

---

## Article 4 — Inspection licence (no Use)

4.1 The Full Owners grant each Recipient a limited, non-exclusive, non-transferable, non-sublicensable right solely to:

(a) view and read Source Code (including via a public repository); and  
(b) store a copy incidental to inspection (including cloning for review).

4.2 Article 4 does not authorise Use, compilation for Use, packaging for deployment, production CI pipelines, Derivative Works (other than private notes), or redistribution.

4.3 Breach by one Recipient does not revoke Article 4 for other non-breaching Recipients of already published copies, without prejudice to remedies and takedown against infringers.

4.4 Licence terms for future distributions may change (Article 15.5). Already obtained copies remain under the DMSAL version that accompanied them.

---

## Article 5 — Contributors: internal Use, Qualifying Delivery, pass-through sublicence

5.1 **Contributor rights.** Each Full Owner may exercise Article 3.2 directly. Each other Contributor may, under that Full Owner’s express written mandate and Article 3.2 as exercised by that Full Owner:

(a) Use, modify, build, package, and embed the Software (including internally);  
(b) include it in Client Software;  
(c) distribute it only as part of, or as required by, a Qualifying Delivery; and  
(d) use Permitted Processors solely for build and delivery.

5.2 **Right to deliver Client Software.** Each Contributor may, without Article 9, create and deliver Client Software that embeds DMSAL components, only by way of a Qualifying Delivery.

5.3 **Pass-through sublicence of Use rights.** Upon a Qualifying Delivery:

(a) the Licensing Party grants the End Beneficiary a pass-through sublicence—non-exclusive, non-transferable, and non-sublicensable except as Article 6.2(c) permits—to Use the exact Delivered Version(s) of each DMSAL component embedded in that Client Software, under Article 6;  

(b) that sublicence runs with the delivered Client Software for runtime of those Delivered Version(s) as embedded therein, including Use by the End Beneficiary’s own end-users solely while using that same Client Software in the ordinary course (including multi-tenant operation of that Client Software for the End Beneficiary’s customers). It is not a standalone licence to extract, copy out, or reuse the components in other products;  

(c) no copyright in the Software is assigned or transferred—only Use rights are sublicensed;  

(d) no separate Article 9 instrument is required for that pass-through sublicence; and  

(e) the sublicence covers only Delivered Version(s) listed in the Delivery Record.

5.4 **No pass-through without Qualifying Delivery.**

(a) Until a Qualifying Delivery exists (Contributor + conforming Delivery Record + not Software-Primary), the End Beneficiary has no Article 5 or Article 6 rights.  
(b) Unauthorized integrators obtain no licence by embedding the Software.  
(c) If the Licensing Party ships Client Software without a conforming Delivery Record, it shall cure by issuing one promptly. Until cure, no Article 6 sublicence arises; as between Licensing Party and End Beneficiary, any interim Use of embedded components is at the Licensing Party’s risk and does not enlarge rights beyond the versions later listed in a conforming Delivery Record.

5.5 Article 5 does not authorise OSI public relicensing or waiver of Article 3.

5.6 Non-Software client code in Client Software is governed by the client contract.

5.7 A Qualifying Delivery plus Article 6 does not require Article 9, except where Article 5.9 or 5.10(b) applies.

5.8 **Licensing Party for a Qualifying Delivery.** The Licensing Party is the Full Owner that ships, or under whose express written mandate the Contributor ships (see Article 1). That Full Owner is the Licensing Party for Articles 3.8, 3.9, and 6.7.

5.9 **Software-Primary (Article 9 required).** A shipment is Software-Primary—and is not Client Software for Articles 5.2–5.3—if its primary purpose is to provide, expose, wrap, or redistribute the Software substantially unmodified. That includes:

1. a thin GUI or API over the Software;  
2. a repackaged module set; or  
3. a shipment where unmodified DMSAL components constitute more than fifty percent (50%) of the shipped proprietary codebase, measured by **file count or by bytes, using the measure that yields the higher DMSAL percentage** (excluding Third-Party Components and generated stubs).

Software-Primary shipments require a Separate Commercial License under Article 9.

5.10 **Resale, OEM, and distribution of Client Software.**

(a) **Covered by Articles 5.3 and 6 (no Article 9):** the End Beneficiary’s Use and operation of the delivered Client Software, including SaaS or multi-tenant operation of that Client Software for its own customers, within Article 6.  

(b) **Requires Article 9 or express written consent of the Licensing Party:**

1. OEM branding of the Software itself;  
2. resale or relicensing of the Software;  
3. distribution of the Software as a standalone library or SDK; and  
4. distribution, sublicensing, or transfer of the Client Software (as a product) to an unaffiliated third party other than the End Beneficiary’s own end-users of that same instance or service under Article 5.3(b) / 6.2(a).

(c) **Illustration.** Hosting end-users on the End Beneficiary’s service is (a). Selling or handing the Client Software codebase or installers to another vendor or integrator for their independent exploitation is (b).

---

## Article 6 — End Beneficiary licence (pass-through sublicence)

6.1 **Formation.** Article 6 rights arise only upon a Qualifying Delivery, when the End Beneficiary accepts the Client Software or places it into production use. Acceptance of a Qualifying Delivery (Delivery Record with DMSAL citation or NOTICE) constitutes acceptance of Article 6 for the embedded Delivered Version(s).

6.2 **Grant.** The pass-through sublicence under Article 5.3 is a non-exclusive, non-transferable right to Use the Software solely:

(a) as part of that delivered Client Software (including runtime for the End Beneficiary’s customers only as users of that same Client Software, not as recipients of a standalone DMSAL licence);  
(b) for Delivered Version(s) only; and  
(c) by Affiliates and Permitted Processors within the same limits, the End Beneficiary remaining responsible.

The End Beneficiary may not assign or further sublicence Article 6 rights except to Affiliates and Permitted Processors as above.

6.3 **Version lock.** There is no general right to other registry or repository versions, or to “latest”, unless delivered or licensed under Article 9. Article 5.3 never extends beyond the Delivered Versions listed for that Qualifying Delivery.

6.4 **Security.** During any active Contracted Related Services period that includes support or warranty, the Licensing Party shall supply security fixes for Delivered Versions within a commercially reasonable time after notice of a critical vulnerability. If the Licensing Party fails to do so within that time, the End Beneficiary may apply a security-only patch (no feature additions) and shall adopt the Licensing Party’s fix when supplied. That patch right is not a general upgrade right.

6.5 **Prohibitions.** Without Article 9 or Licensing Party consent under Article 5.10(b): no extraction for unrelated products; no standalone redistribution of the Software; no licensing the Software apart from runtime of the delivered Client Software; no claim of ownership of the Software; no OEM, resale, or Client Software redistribution beyond Article 5.10(a).

6.6 **Client master agreements (priority).**

(a) As between Licensing Party and End Beneficiary, a signed client master or engagement contract prevails over this Licence on conflicting points, except as stated in (b)–(e).  

(b) Copyright in the Software is not assigned by silence or by generic “work product” / “assignment of deliverables” language unless that language expressly names the Software modules and is executed as (or with) an Article 9 instrument by a Full Owner with Independent Commercialization Authority.  

(c) A Full Owner shall not purport to assign the other Full Owner’s Contributions contrary to Article 3. Any such purport binds only the signing Full Owner’s own Contributions to the extent lawfully assignable, and constitutes a breach of this Licence as between Full Owners.  

(d) Narrower use restrictions in the client contract bind the End Beneficiary in addition to Article 6.  

(e) Article 5.3 grants a sublicence of Use rights only; it does not assign copyright.

6.7 **Services.** Contracted Related Services are owed solely by the Licensing Party (Article 3.9).

---

## Article 7 — Restrictions

Except under Articles 4–6 or Article 9, Recipients shall not:

- Use the Software;  
- compile, build, or deploy for Use;  
- create Derivative Works (except Contributors under Article 5 and security patches under Article 6.4);  
- copy except as incidental to Article 4;  
- distribute or sublicence standalone (except Contributors under a Qualifying Delivery within Articles 5.2–5.3);  
- remove notices;  
- misuse marks as endorsement;  
- circumvent controls; or  
- act unlawfully.

Integrators without Contributor status obtain no rights by embedding the Software. All other rights are reserved.

---

## Article 8 — Brand; non-OSI; trademarks

8.1 Digital Mind is common branding only: not a legal person; not a copyright owner; not a required signatory. It creates no joint venture or joint services liability.

8.2 Each Full Owner may use the Digital Mind brand on Software it licences or delivers under this Licence. Ownership of the trademark “Digital Mind” as between DSE and MS is governed by their separate arrangements. Neither shall obstruct the other’s exercise of Independent Commercialization Authority solely by trademark claims without a separate written trademark exclusive.

8.3 This Licence is not OSI-approved. Public Source Code does not mean free Use. Copyleft combination that would force broader grants is prohibited.

---

## Article 9 — Separate Commercial License

9.1 Required for exploitation outside Articles 4–6, including:

- Software-Primary shipments (Article 5.9); and  
- OEM, resale, or Client Software redistribution beyond Article 5.10(a).

9.2 Either Full Owner may execute alone, pursuant to Independent Commercialization Authority. Co-signature is not required for validity toward the licensee.

9.3 Non-exclusive unless Article 3.6 consent to exclusivity is obtained.

9.4 The Licensing Party undertakes Contracted Related Services for that licence (Article 3.9). Failure of services is a breach of the Licensing Party, not a title defect of the other Full Owner.

9.5 Commercial terms are fixed only in that instrument.

9.6 Contacts: DSE — Lucian Laurențiu Dragomir; MS — Răzvan-Gabriel Ionescu.

---

## Article 10 — Patents; third-party; export

10.1 Each Full Owner grants Recipients under Articles 4–6 and 9 a limited, non-exclusive, royalty-free licence under patent claims it owns that are necessarily infringed by the unmodified Software, solely to exercise those Articles; and grants the other Full Owner a reciprocal licence under such claims as needed for Independent Commercialization Authority. Patent licences terminate as to a party that files patent litigation alleging the Software infringes its patents.

10.2 Third-Party Components remain under their own licences.

10.3 Export and sanctions laws must be observed.

---

## Article 11 — Disclaimer (quality)

EXCEPT FOR ARTICLES 3.8, 3.9, 6.4, AND 9.4, AND EXCEPT FOR ANY EXPRESS WRITTEN CUSTOMER TERMS, THE SOFTWARE IS PROVIDED “AS IS” AND “AS AVAILABLE”, WITHOUT WARRANTY OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE. THE IP INDEMNITY IN ARTICLE 3.8(d) REMAINS SUBJECT ONLY TO ITS OWN CAP UNDER THAT ARTICLE.

---

## Article 12 — Liability

12.1 To the maximum extent permitted by law: no indirect, incidental, special, consequential, exemplary, or punitive damages, and no liability for loss of profits, revenue, data, or goodwill.

12.2 Where liability cannot be excluded:

(a) Contracted Related Services and customer-facing duties bind the Licensing Party only;  

(b) the Licensing Party’s aggregate liability for claims other than Article 3.8(d) IP indemnity is limited to the greater of EUR 1,000 or fees paid to that Licensing Party for the relevant Client Software or Separate Commercial License in the twelve (12) months before the claim;  

(c) Article 3.8(d) IP indemnity is subject only to its own cap under Article 3.8(d) and is not reduced by Article 12.2(b);  

(d) for Article 4-only Recipients who paid nothing — liability of EUR 0.

12.3 Mandatory Romanian law (including wilful misconduct where exclusion is void) is unaffected.

---

## Article 13 — Suspension; survival

13.1 Article 4 rights of a breaching Recipient terminate upon breach.

13.2 Article 5 rights of a Full Owner in material breach may be suspended after thirty (30) days’ notice without cure. Article 3.2 grants remain as needed to support vested Article 6 rights, Article 9 licences already granted, and Contracted Related Services on those deals. Independent Commercialization Authority already exercised in favour of customers is not retroactively voided.

13.3 Article 6 ends as to an End Beneficiary upon material breach of Article 6 or unlawful Use of the Client Software in relation to the Software.

13.4 The following survive: Articles 3, 6 (for vested End Beneficiary rights until ended under Article 13.3), 7, 8, 10, 11, 12, 14, and 15. Article 9 remains in force for licences already executed. Article 5 survives only as needed to explain vested Article 6 rights.

---

## Article 14 — Enforcement; repositories

14.1 Public hosting does not waive copyright or create an OSI licence.

14.2 Either Full Owner may enforce against infringement outside authorised grants. Non-enforcement against one infringer is not a waiver.

14.3 Public repositories should include this Licence as `LICENSE` (or a clear pointer) and NOTICE per Schedule B.

---

## Article 15 — General

15.1 **Entire public terms.** This Licence is the public copyright framework. Customer commercial terms are in Separate Commercial Licenses and engagement documents. Article 6.6 governs conflicts with master agreements.

15.2 Severability; no waiver by silence; no assignment by Recipients. A Full Owner may assign Article 5 rights to a corporate successor on notice to the other. Customer grants already made remain in force.

15.3 **Governing law; venue.** Romania; courts of Bucharest, unless a Separate Commercial License or client engagement validly provides otherwise for that engagement.

15.4 **Language.** English prevails; translations are courtesy. Schedule D is informative only.

15.5 **Versions.** Later DMSAL versions apply only to future distributions under that later text. A given copy remains under the version that accompanied it unless agreed otherwise in writing.

15.6 **Hierarchy.** Articles prevail over Schedules A and D. Schedules B and C are operative notice templates when used as such.

15.7 **Inter-company deed.** A separate signed acknowledgement between DSE and MS mirroring Article 3 is recommended for evidentiary comfort. Absence of that deed does not negate Article 3 as between Full Owners for Software published under this Licence. Confirmation-letter timing and content are governed solely by Article 3.8(f).

---

## Schedule A — Plain-language summary (non-operative)

| Topic | Rule |
|-------|------|
| Digital Mind | Brand only. |
| Ownership | Each Full Owner owns its Contributions; each has Independent Commercialization Authority to licence the whole alone. |
| Temporal | Governs distributions under this text; older deals without DMSAL keep their own terms until adopted or republished. |
| Services | Only Contracted Related Services, owed by the Licensing Party. |
| Contributors | Full Owner, or person with express written Full Owner mandate. |
| Qualifying Delivery | Contributor + Delivery Record (listed versions + minimum SBOM + DMSAL/NOTICE) + not Software-Primary. |
| Pass-through | Sublicence of Use for listed Delivered Versions only; no copyright transfer; no Art. 6 rights until Delivery Record conforms. |
| Software-Primary | Thin wrap, or >50% DMSAL by the higher of file count or bytes → Article 9. |
| Client Software to third parties | End-user SaaS of the delivered product: Articles 5–6. Codebase/installers to another vendor: Article 9 (5.10). |
| IP indemnity | Separate cap: greater of 2× fees or EUR 25,000, unless Article 9 raises it. |
| Confirmation | Article 3.8(f): letter in 15 Business Days; efforts for co-confirmation or redacted deed extract. |

---

## Schedule B — NOTICE

```text
Artifact: <name>
Licence: DMSAL v1.0 (effective 1 January 2020 for distributions under this text)
Brand: Digital Mind (branding only)
Copyright: Contributions — DIGITAL SOLUTIONS EXPERT S.R.L. and/or MIND STUDIO S.R.L.
Commercialization: either Full Owner alone (Independent Commercialization Authority)
Contributors: Full Owner or express written mandate only
Pass-through: Qualifying Delivery (Delivery Record + SBOM + listed Delivered Versions) → sublicence of Use
Software-Primary / OEM / Client Software redistribution to other vendors: Art. 9
IP indemnity cap (separate): greater of 2× fees or EUR 25,000, unless Art. 9 states higher
Related Services: Contracted Related Services owed solely by the Licensing Party
```

---

## Schedule C — Copyright header

```text
Copyright (c) 2017–present DIGITAL SOLUTIONS EXPERT S.R.L. and/or MIND STUDIO S.R.L.
Digital Mind = branding only.
Licensed under DMSAL v1.0. Either Full Owner may commercialise alone under Independent Commercialization Authority.
Qualifying Delivery: pass-through sublicence of Use for embedded Delivered Versions (not a copyright transfer).
Software-Primary or OEM/resale of modules requires Art. 9.
Licensing Party owes Contracted Related Services only.
Public Source Code: inspection only. Use: Articles 5, 6, or 9.
See LICENSE.md
```

---

## Schedule D — Rezumat (RO) — informativ

- **Digital Mind** = branding comun.  
- **Copyright:** fiecare Full Owner pe contribuțiile sale; **autoritate de comercializare independentă** — oricare licențiază singur față de client.  
- Model: proprietate pe Contributions + licențe reciproce de comercializare.  
- **DMSAL** guvernează distribuțiile sub acest text; contractele vechi fără referință rămân pe termenii lor până la adoptare expresă sau republicare.  
- **Contributors:** Full Owner sau mandat scris expres.  
- **Qualifying Delivery:** Contributor + Delivery Record (versiuni listate + SBOM minim + DMSAL/NOTICE) + nu Software-Primary.  
- **Pass-through:** sublicență de Use pe versiunile listate; fără Delivery Record conform → fără drepturi Art. 6.  
- SaaS pentru end-users pe Client Software-ul livrat: Art. 5–6. Predare codebase/instalatori către alt vendor: Art. 9 (5.10).  
- **Indemnity IP:** cap separat — maximul dintre 2× fees și EUR 25.000.  
- **Confirmation letter** (Art. 3.8(f)): 15 zile lucrătoare; efort co-confirmare / extras deed.  
- Prevalează EN, Articolele 1–15.

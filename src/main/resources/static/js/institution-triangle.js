(function () {
    'use strict';

    const DATA = {
        cdac: {
            title: 'C-DAC',
            description: 'Advanced computing, digital technologies and strategic technology solutions.',
            domains: [
                ['Quantum Computing', 'stars', 'Exploring quantum technologies, algorithms and next-generation computing capabilities.'],
                ['High Performance Computing', 'cpu-fill', 'Advanced computing infrastructure, parallel processing and scientific workloads.'],
                ['Artificial Intelligence', 'robot', 'AI, machine learning and intelligent solutions for real-world public and industrial applications.'],
                ['RISC-V & Digital Systems', 'cpu', 'Open processor architectures, embedded systems and indigenous digital technology.'],
                ['Cyber Security', 'shield-lock', 'Cybersecurity technologies, secure systems and resilient digital infrastructure.'],
                ['Software Solutions', 'code-slash', 'Enterprise software, digital platforms and technology-enabled transformation.'],
                ['Strategic Sector', 'flag', 'Mission-critical technology for strategic, national and high-impact applications.'],
                ['Blockchain', 'link-45deg', 'Distributed-ledger technologies and trusted digital transaction systems.']
            ]
        },
        sameer: {
            title: 'SAMEER',
            description: 'Electronics, RF, communications, instrumentation and applied electromagnetic technologies.',
            domains: [
                ['Nxt Gen Communication Technology', 'reception-4', 'Advanced wireless, RF and communication technologies for connected systems.'],
                ['EMI / EMC / EMP & Safety', 'shield-check', 'Testing, measurement and consulting for electromagnetic compatibility and safety.'],
                ['Atmospheric Instrumentation', 'cloud-sun', 'Sensing and instrumentation technologies for atmospheric and environmental applications.'],
                ['Medical Devices', 'heart-pulse-fill', 'Electronics and instrumentation for healthcare, diagnostics and medical applications.'],
                ['Smart IoT', 'broadcast', 'Connected sensing, embedded electronics and intelligent IoT systems.'],
                ['Industrial Applications', 'gear-wide-connected', 'Applied electronics, RF and instrumentation solutions for industrial environments.']
            ]
        },
        cmet: {
            title: 'C-MET',
            description: 'Materials, electronics, semiconductor technologies and advanced energy solutions.',
            domains: [
                ['Semiconductor Materials & Devices', 'memory', 'Advanced materials, semiconductor processes and device technologies.'],
                ['Power Electronics', 'lightning-charge', 'Power semiconductor technologies and efficient electronic power systems.'],
                ['Additive Manufacturing & Packaging', 'box-seam', 'Advanced manufacturing and electronics packaging technologies.'],
                ['Energy Generation & Storage', 'battery-charging', 'Materials and technologies for energy conversion, generation and storage.'],
                ['E-waste & RoHS', 'recycle', 'Sustainable electronics, e-waste management and restricted-substance compliance.'],
                ['Electronics Materials', 'layers', 'Functional materials and advanced materials research for electronics applications.']
            ]
        }
    };

    const nodes = document.querySelectorAll('.institution-node');
    const panel = document.getElementById('institution-domain-panel');
    const title = document.getElementById('institution-panel-title');
    const description = document.getElementById('institution-panel-description');
    const list = document.getElementById('institution-domain-list');
    const content = document.getElementById('institution-content');
    const connector = document.getElementById('institution-active-connector');
    const connectorDot = document.getElementById('institution-connector-dot');

    if (!nodes.length || !panel || !title || !description || !list || !content || !connector || !connectorDot) {
        return;
    }

    const connectorPaths = {
        cdac: 'M700 100 C820 105 900 125 985 145',
        sameer: 'M175 640 C210 520 300 335 430 165',
        cmet: 'M1225 640 C1190 520 1100 335 970 165'
    };

    const connectorDots = {
        cdac: { cx: 985, cy: 145 },
        sameer: { cx: 430, cy: 165 },
        cmet: { cx: 970, cy: 165 }
    };

    function animatePanel() {
        panel.classList.remove('is-visible');
        void panel.offsetWidth;
        panel.classList.add('is-visible');
    }

    function setConnector(key) {
        connector.setAttribute('d', connectorPaths[key]);

        connector.classList.remove('is-travelling');
        void connector.getBoundingClientRect();
        connector.classList.add('is-travelling');

        connectorDot.setAttribute('cx', connectorDots[key].cx);
        connectorDot.setAttribute('cy', connectorDots[key].cy);
    }

    function renderDomain(institution, domain, selectedButton) {
        list.querySelectorAll('.institution-domain-chip')
            .forEach(button => button.classList.remove('is-selected'));

        if (selectedButton) {
            selectedButton.classList.add('is-selected');
        }

        content.innerHTML = `
            <div class="domain-content-main">
                <span class="domain-content-icon">
                    <i class="bi bi-${domain[1]}" aria-hidden="true"></i>
                </span>
                <div>
                    <span class="institution-panel-kicker">${institution.title} · THEMATIC DOMAIN</span>
                    <h4>${domain[0]}</h4>
                    <p>${domain[2]}</p>
                </div>
            </div>
        `;

        content.style.animation = 'none';
        void content.offsetWidth;
        content.style.animation = '';
    }

    function renderInstitution(key, focusDomain = 0) {
        const institution = DATA[key];
        if (!institution) return;

        panel.dataset.activeInstitution = key;

        panel.classList.remove(
            'institution-domain-panel--right',
            'institution-domain-panel--top-left',
            'institution-domain-panel--top-right'
        );

        if (key === 'cdac') {
            panel.classList.add('institution-domain-panel--right');
        } else if (key === 'sameer') {
            panel.classList.add('institution-domain-panel--top-left');
        } else {
            panel.classList.add('institution-domain-panel--top-right');
        }

        title.textContent = institution.title;
        description.textContent = institution.description;
        list.innerHTML = '';

        nodes.forEach(node => {
            const active = node.dataset.institution === key;
            node.classList.toggle('is-active', active);
            node.setAttribute('aria-pressed', active ? 'true' : 'false');
        });

        institution.domains.forEach((domain, index) => {
            const button = document.createElement('button');

            button.type = 'button';
            button.className = 'institution-domain-chip';
            button.setAttribute('role', 'listitem');
            button.innerHTML =
                '<i class="bi bi-' + domain[1] + '" aria-hidden="true"></i>' +
                '<span>' + domain[0] + '</span>';

            button.addEventListener('click', () => {
                renderDomain(institution, domain, button);
            });

            list.appendChild(button);

            if (focusDomain === index) {
                renderDomain(institution, domain, button);
            }
        });

        setConnector(key);
        animatePanel();
    }

    nodes.forEach(node => {
        node.addEventListener('click', () => {
            renderInstitution(node.dataset.institution, 0);
        });
    });

    renderInstitution('cdac', 0);
})();

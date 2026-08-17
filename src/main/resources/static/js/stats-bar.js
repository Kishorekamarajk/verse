(function () {
    var COUNT_DURATION = 1400;

    function reveal(items) {
        items.forEach(function (el, index) {
            window.setTimeout(function () {
                el.classList.add('is-visible');
            }, index * 0); // stagger handled via --stat-delay in CSS
        });
    }

    function easeOutQuad(t) {
        return 1 - (1 - t) * (1 - t);
    }

    function animateCount(el, reduceMotion) {
        if (!el.hasAttribute('data-stat-target')) return;

        var target = parseInt(el.getAttribute('data-stat-target'), 10) || 0;

        if (reduceMotion) {
            el.textContent = target;
            return;
        }

        var start = null;

        function step(timestamp) {
            if (start === null) start = timestamp;
            var progress = Math.min((timestamp - start) / COUNT_DURATION, 1);
            el.textContent = Math.round(target * easeOutQuad(progress));
            if (progress < 1) {
                requestAnimationFrame(step);
            } else {
                el.textContent = target;
            }
        }

        requestAnimationFrame(step);
    }

    function showStatSuffix(el) {
        var suffix = el.parentElement && el.parentElement.querySelector('[data-stat-suffix]');
        if (suffix) {
            suffix.hidden = false;
        }
    }

    function setStatistics(statValues, data) {
        statValues.forEach(function (el) {
            var key = el.getAttribute('data-stat-key');
            var value = Number(data && data[key]);
            if (!Number.isFinite(value) || value < 0) {
                return;
            }
            el.setAttribute('data-stat-target', Math.floor(value).toString());
            showStatSuffix(el);
        });
    }

    function loadStatistics(root, statValues) {
        return fetch(root.getAttribute('data-statistics-url') || '/api/statistics', {
            headers: { 'Accept': 'application/json' },
            credentials: 'same-origin'
        }).then(function (response) {
            if (!response.ok) {
                throw new Error('Failed to load statistics');
            }
            return response.json();
        }).then(function (data) {
            setStatistics(statValues, data);
        }).catch(function (error) {
            console.error('Error loading statistics:', error);
        });
    }

    function initStatsBar() {
        var root = document.querySelector('[data-stats-reveal]');
        if (!root) return;

        var revealItems = root.querySelectorAll('[data-reveal-hidden]');
        var statValues = root.querySelectorAll('[data-stat-value]');
        var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;
        var statisticsReady = loadStatistics(root, statValues);

        function triggerAll() {
            reveal(revealItems);
            statisticsReady.then(function () {
                statValues.forEach(function (el) { animateCount(el, reduceMotion); });
            });
        }

        if (reduceMotion || !('IntersectionObserver' in window)) {
            triggerAll();
            return;
        }

        var observer = new IntersectionObserver(function (entries, obs) {
            entries.forEach(function (entry) {
                if (!entry.isIntersecting) return;
                triggerAll();
                obs.disconnect();
            });
        }, { threshold: 0.2, rootMargin: '0px 0px -60px 0px' });

        observer.observe(root);
    }

    document.addEventListener('DOMContentLoaded', initStatsBar);
})();

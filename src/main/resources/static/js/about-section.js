(function () {
    var SETTLE_CLASSES = ['opacity-100', 'translate-x-0', 'translate-y-0', 'scale-100'];
    var HIDDEN_CLASSES = ['opacity-0', 'translate-y-4', 'translate-y-6', '-translate-y-4', '-translate-x-10', 'scale-95'];
    var COUNT_DURATION = 1400;

    function reveal(items) {
        items.forEach(function (el) {
            HIDDEN_CLASSES.forEach(function (cls) { el.classList.remove(cls); });
            SETTLE_CLASSES.forEach(function (cls) { el.classList.add(cls); });
        });
    }

    function easeOutQuad(t) {
        return 1 - (1 - t) * (1 - t);
    }

    function animateCount(el, reduceMotion) {
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

    function initAboutSection() {
        var root = document.querySelector('[data-about-reveal]');
        if (!root) return;

        var revealItems = root.querySelectorAll('[data-reveal-hidden]');
        var statValues = root.querySelectorAll('[data-stat-value]');
        var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

        function triggerAll() {
            reveal(revealItems);
            statValues.forEach(function (el) { animateCount(el, reduceMotion); });
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
        }, { threshold: 0.15, rootMargin: '0px 0px -80px 0px' });

        observer.observe(root);
    }

    document.addEventListener('DOMContentLoaded', initAboutSection);
})();

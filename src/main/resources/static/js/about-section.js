(function () {
    var SETTLE_CLASSES = ['opacity-100', 'translate-x-0', 'translate-y-0', 'scale-100'];
    var HIDDEN_CLASSES = ['opacity-0', 'translate-y-4', 'translate-y-6', '-translate-y-4', '-translate-x-10', 'scale-95'];

    function reveal(items) {
        items.forEach(function (el) {
            HIDDEN_CLASSES.forEach(function (cls) { el.classList.remove(cls); });
            SETTLE_CLASSES.forEach(function (cls) { el.classList.add(cls); });
        });
    }

    function initAboutSection() {
        var root = document.querySelector('[data-about-reveal]');
        if (!root) return;

        var revealItems = root.querySelectorAll('[data-reveal-hidden]');
        var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

        function triggerAll() {
            reveal(revealItems);
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

(function () {
    var SETTLE_CLASSES = ['opacity-100', 'translate-y-0', 'scale-100'];
    var HIDDEN_CLASSES = ['opacity-0', 'translate-y-8', 'translate-y-6', 'scale-75'];

    function settle(el) {
        HIDDEN_CLASSES.forEach(function (cls) { el.classList.remove(cls); });
        SETTLE_CLASSES.forEach(function (cls) { el.classList.add(cls); });
    }

    function ready(fn) {
        if (document.readyState !== 'loading') {
            fn();
        } else {
            document.addEventListener('DOMContentLoaded', fn);
        }
    }

    function sizeHeroVideo() {
        var wrap = document.querySelector('[data-hero-video-wrap]');
        var iframe = document.querySelector('[data-hero-video]');
        if (!wrap || !iframe) return;

        var videoRatio = 16 / 9;
        var w = wrap.clientWidth;
        var h = wrap.clientHeight;
        var containerRatio = w / h;

        var newW, newH;
        if (containerRatio > videoRatio) {
            newW = w;
            newH = w / videoRatio;
        } else {
            newH = h;
            newW = h * videoRatio;
        }

        iframe.style.width = newW + 'px';
        iframe.style.height = newH + 'px';
    }

    ready(function () {
        var revealItems = document.querySelectorAll('[data-hero-reveal]');
        var videoWrap = document.querySelector('[data-hero-video-wrap]');
        var reduceMotion = window.matchMedia && window.matchMedia('(prefers-reduced-motion: reduce)').matches;

        sizeHeroVideo();
        window.addEventListener('resize', sizeHeroVideo);

        if (reduceMotion) {
            revealItems.forEach(settle);
            if (videoWrap) {
                videoWrap.classList.remove('opacity-0');
                videoWrap.classList.add('opacity-100');
            }
            return;
        }

        requestAnimationFrame(function () {
            if (videoWrap) {
                videoWrap.classList.remove('opacity-0');
                videoWrap.classList.add('opacity-100');
            }
            revealItems.forEach(settle);
        });
    });
})();

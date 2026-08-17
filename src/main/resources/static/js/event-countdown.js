(function () {
    'use strict';

    var TARGET = new Date('2026-11-26T10:00:00+05:30').getTime();
    var blocks = document.querySelectorAll('[data-event-countdown]');
    if (!blocks.length) return;

    function setValues(block, d, h, m, s) {
        var map = {
            days: d,
            hours: h,
            minutes: m,
            seconds: s
        };
        Object.keys(map).forEach(function (key) {
            var el = block.querySelector('[data-event-' + key + ']');
            if (el) el.textContent = String(map[key]).padStart(2, '0');
        });
    }

    function update() {
        var remaining = TARGET - Date.now();
        var isLive = remaining <= 0;
        var total = Math.max(0, Math.floor(remaining / 1000));
        var days = Math.floor(total / 86400);
        var hours = Math.floor((total % 86400) / 3600);
        var minutes = Math.floor((total % 3600) / 60);
        var seconds = total % 60;

        blocks.forEach(function (block) {
            var countdown = block.querySelector(':scope > .hero-event-countdown, :scope > .about-event-status__countdown');
            var live = block.querySelector('[data-event-live]');
            setValues(block, days, hours, minutes, seconds);

            if (isLive) {
                if (countdown) countdown.hidden = true;
                if (live) live.hidden = false;
                block.classList.add('event-is-live');
            } else {
                if (countdown) countdown.hidden = false;
                if (live) live.hidden = true;
                block.classList.remove('event-is-live');
            }
        });
    }

    function init() {
        update();
        window.setInterval(update, 1000);
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init, { once: true });
    } else {
        init();
    }
})();

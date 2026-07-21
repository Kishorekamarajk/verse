(() => {
    const tabs = document.querySelectorAll("[data-gallery-tab]");
    const panels = document.querySelectorAll("[data-gallery-year]");
    if (!tabs.length) return;

    const setYear = (year) => {
        tabs.forEach((tab) => tab.classList.toggle("is-active", tab.dataset.galleryTab === year));
        panels.forEach((panel) => panel.classList.toggle("is-active", panel.dataset.galleryYear === year));
    };

    tabs.forEach((tab) => {
        tab.addEventListener("click", () => setYear(tab.dataset.galleryTab));
    });
})();

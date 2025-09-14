export function navigateTo(screen: HTMLElement): void {
	const appContainer: HTMLBodyElement | null = document.querySelector("body");
	if (appContainer) {
		appContainer.innerHTML = "";
		appContainer.appendChild(screen);
	}
}

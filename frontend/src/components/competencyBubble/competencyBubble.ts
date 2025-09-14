import styles from "./competencyBubble.module.css";

export function competencyBubble(name: string): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	container.textContent = name;
	container.className = styles.competencyBubble;
	return container;
}

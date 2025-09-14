import styles from "./registrationScreen.module.css";

import { registrationForm } from "../../components/registrationForm/registrationForm";
import { loginForm } from "../../components/loginForm/loginForm";

export function registrationScreen(): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");
	container.className = styles.registrationScreen;

	const titleContainer: HTMLElement = document.createElement("header");
	titleContainer.className = styles.registrationTitleContainer;

	const appName: HTMLHeadingElement = document.createElement("h1");
	const title1: HTMLSpanElement = document.createElement("span");
	title1.textContent = "Linke";
	title1.classList.add(styles.registrationTitle, styles.registrationTitle1);

	const title2: HTMLSpanElement = document.createElement("span");
	title2.textContent = "Tinder";
	title2.classList.add(styles.registrationTitle, styles.registrationTitle2);

	appName.appendChild(title1);
	appName.appendChild(title2);
	titleContainer.appendChild(appName);

	const registration: HTMLElement = registrationForm();
	const login: HTMLDivElement = loginForm();
	titleContainer.appendChild(login);
	container.appendChild(titleContainer);
	container.appendChild(registration);

	return container;
}

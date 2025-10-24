import styles from "./registrationScreen.module.css";

import { registrationForm } from "../../components/registrationForm/registrationForm";
import { loginForm } from "../../components/loginForm/loginForm";
import type { AppContext } from "../../utils/AppContext";

export function registrationScreen(appContext: AppContext): HTMLDivElement {
	const container = createScreenContainer();
	const header = createHeader();
	const registration = registrationForm(appContext);
	const login = loginForm(appContext);

	header.appendChild(login);
	container.appendChild(header);
	container.appendChild(registration);

	return container;
}

function createScreenContainer(): HTMLDivElement {
	const container = document.createElement("div");
	container.className = styles.registrationScreen;
	return container;
}

function createHeader(): HTMLElement {
	const header = document.createElement("header");
	header.className = styles.registrationTitleContainer;

	const appName = document.createElement("h1");
	const title1 = document.createElement("span");
	title1.textContent = "Linke";
	title1.classList.add(styles.registrationTitle, styles.registrationTitle1);

	const title2 = document.createElement("span");
	title2.textContent = "Tinder";
	title2.classList.add(styles.registrationTitle, styles.registrationTitle2);

	appName.appendChild(title1);
	appName.appendChild(title2);
	header.appendChild(appName);

	return header;
}

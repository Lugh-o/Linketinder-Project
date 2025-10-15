import styles from "./header.module.css";

import { Candidate } from "../../types/Candidate";
import PersonIcon from "../../assets/person.svg";
import { registrationScreen } from "../../pages/registrationScreen/registrationScreen";
import { navigateTo } from "../../utils/router";

export function candidateHeader(candidate: Candidate): HTMLElement {
	const container: HTMLElement = document.createElement("header");
	container.className = styles.headerContainer;

	const appName: HTMLHeadingElement = document.createElement("h1");
	appName.className = styles.appName;
	const title1: HTMLElement = document.createElement("span");
	title1.textContent = "Linke";
	title1.className = styles.title1;

	const title2: HTMLElement = document.createElement("span");
	title2.textContent = "Tinder";
	title2.className = styles.title2;

	appName.appendChild(title1);
	appName.appendChild(title2);

	const profileWrapper: HTMLDivElement = document.createElement("div");
	profileWrapper.className = styles.profileWrapper;
	const image: HTMLImageElement = document.createElement("img");
	image.src = PersonIcon;

	profileWrapper.appendChild(image);

	const name: HTMLHeadingElement = document.createElement("h4");
	name.textContent = candidate.name;
	profileWrapper.appendChild(name);

	const logoutButton: HTMLButtonElement = document.createElement("button");
	logoutButton.textContent = "Logout";
	logoutButton.className = styles.logoutButton;

	logoutButton.addEventListener("click", () => {
		navigateTo(registrationScreen());
	});

	appName.appendChild(logoutButton);

	container.appendChild(appName);
	container.appendChild(profileWrapper);

	return container;
}

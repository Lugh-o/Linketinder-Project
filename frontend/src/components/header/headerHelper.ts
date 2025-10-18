import styles from "./header.module.css";

import { Person } from "../../types/Person";
import PersonIcon from "../../assets/person.svg";
import CompanyIcon from "../../assets/company.svg";
import { Candidate } from "../../types/Candidate";

export function buildHeader<T extends Person>(
	person: T,
	buildAppName: (person: T) => HTMLHeadingElement
) {
	const container: HTMLElement = document.createElement("header");
	container.className = styles.headerContainer;

	const appName: HTMLHeadingElement = buildAppName(person);
	const profile: HTMLDivElement = buildProfile(person);

	container.appendChild(appName);
	container.appendChild(profile);

	return container;
}

export function buildCommonAppName(): HTMLHeadingElement {
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
	return appName;
}

export function createHeaderButton(
	label: string,
	className: string,
	onClick: () => void
): HTMLButtonElement {
	const button = document.createElement("button");
	button.textContent = label;
	button.className = className;
	button.addEventListener("click", onClick);
	return button;
}

function buildProfile<T extends Person>(person: T): HTMLDivElement {
	const wrapper = document.createElement("div");
	wrapper.className = styles.profileWrapper;

	const image = document.createElement("img");
	if (person instanceof Candidate) {
		image.src = PersonIcon;
	} else {
		image.src = CompanyIcon;
	}

	const name = document.createElement("h4");
	name.textContent = person.name;

	wrapper.appendChild(image);
	wrapper.appendChild(name);

	return wrapper;
}

import styles from "./header.module.css";

import { Candidate } from "../../types/Candidate";
import { registrationScreen } from "../../pages/registrationScreen/registrationScreen";
import { navigateTo } from "../../utils/router";
import { buildHeader } from "./headerHelper";
import { createHeaderButton, buildCommonAppName } from "./headerHelper";

export function candidateHeader(candidate: Candidate): HTMLElement {
	return buildHeader(candidate, buildAppName);
}

function buildAppName(): HTMLHeadingElement {
	const appName = buildCommonAppName();

	const logoutButton = createHeaderButton("Logout", styles.logoutButton, () =>
		navigateTo(registrationScreen())
	);
	appName.appendChild(logoutButton);

	return appName;
}

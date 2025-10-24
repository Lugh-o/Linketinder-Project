import styles from "./header.module.css";

import { Candidate } from "../../types/Candidate";
import { buildHeader } from "./headerHelper";
import { createHeaderButton, buildCommonAppName } from "./headerHelper";
import type { AppContext } from "../../utils/AppContext";

export function candidateHeader(
	candidate: Candidate,
	appContext: AppContext
): HTMLElement {
	return buildHeader(candidate, () =>
		buildAppName(() => appContext.router.goToRegistration(appContext))
	);
}

function buildAppName(navigator: () => void): HTMLHeadingElement {
	const appName = buildCommonAppName();

	const logoutButton = createHeaderButton(
		"Logout",
		styles.logoutButton,
		navigator
	);
	appName.appendChild(logoutButton);

	return appName;
}

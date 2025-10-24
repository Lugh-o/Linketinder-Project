import { loginFormCandidate } from "./loginFormCandidate";
import { loginFormCompany } from "./loginFormCompany";
import switchControls from "../switchControls/switchControls";
import type { AppContext } from "../../utils/AppContext";

export function loginForm(appContext: AppContext): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");

	const formArea: HTMLDivElement = document.createElement("div");

	function showCandidateLogin(): void {
		formArea.innerHTML = "";
		formArea.appendChild(loginFormCandidate(appContext));
	}

	function showCompanyLogin(): void {
		formArea.innerHTML = "";
		formArea.appendChild(loginFormCompany(appContext));
	}

	const switchLoginControls: HTMLDivElement = switchControls(
		showCandidateLogin,
		showCompanyLogin
	);

	container.appendChild(switchLoginControls);
	container.appendChild(formArea);

	showCandidateLogin();

	return container;
}

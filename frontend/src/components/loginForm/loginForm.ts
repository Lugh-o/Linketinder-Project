import { loginFormCandidate } from "./loginFormCandidate";
import { loginFormCompany } from "./loginFormCompany";
import switchControls from "../switchControls/switchControls";

export function loginForm(): HTMLDivElement {
	const container: HTMLDivElement = document.createElement("div");

	const formArea: HTMLDivElement = document.createElement("div");

	function showCandidateLogin(): void {
		formArea.innerHTML = "";
		formArea.appendChild(loginFormCandidate());
	}

	function showCompanyLogin(): void {
		formArea.innerHTML = "";
		formArea.appendChild(loginFormCompany());
	}

	const switchLoginControls: HTMLDivElement = switchControls(
		showCandidateLogin,
		showCompanyLogin
	);

	container.appendChild(switchLoginControls);
	container.appendChild(formArea);

	// default
	showCandidateLogin();

	return container;
}

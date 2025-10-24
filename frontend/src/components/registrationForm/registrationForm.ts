import { registrationFormCompany } from "./registrationFormCompany";
import { registrationFormCandidate } from "./registrationFormCandidate";
import styles from "./registrationForm.module.css";
import switchControls from "../switchControls/switchControls";
import type { AppContext } from "../../utils/AppContext";

export function registrationForm(appContext: AppContext): HTMLElement {
	const container: HTMLElement = document.createElement("main");
	container.className = styles.formContainer;

	const formArea: HTMLDivElement = document.createElement("div");
	formArea.className = styles.formArea;

	function showCandidateForm(): void {
		formArea.innerHTML = "";
		formArea.appendChild(registrationFormCandidate(appContext));
	}

	function showCompanyForm(): void {
		formArea.innerHTML = "";
		formArea.appendChild(registrationFormCompany(appContext));
	}

	const switchRegistrationControls = switchControls(
		() => showCandidateForm(),
		() => showCompanyForm()
	);

	container.appendChild(switchRegistrationControls);
	container.appendChild(formArea);

	showCandidateForm();

	return container;
}

import { registrationFormCompany } from "./registrationFormCompany";
import { registrationFormCandidate } from "./registrationFormCandidate";
import styles from "./registrationForm.module.css";
import switchControls from "../switchControls/switchControls";

export function registrationForm(): HTMLElement {
	const container: HTMLElement = document.createElement("main");
	container.className = styles.formContainer;

	const formArea: HTMLDivElement = document.createElement("div");
	formArea.className = styles.formArea;

	function showCandidateForm(): void {
		formArea.innerHTML = "";
		formArea.appendChild(registrationFormCandidate());
	}

	function showCompanyForm(): void {
		formArea.innerHTML = "";
		formArea.appendChild(registrationFormCompany());
	}

	const switchRegistrationControls = switchControls(
		showCandidateForm,
		showCompanyForm
	);

	container.appendChild(switchRegistrationControls);
	container.appendChild(formArea);

	showCandidateForm();

	return container;
}

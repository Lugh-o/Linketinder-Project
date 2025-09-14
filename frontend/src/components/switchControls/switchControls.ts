import styles from "./switchControls.module.css";

export default function switchControls(
	onCandidateClick: () => void,
	onCompanyClick: () => void
): HTMLDivElement {
	const switchWrapper: HTMLDivElement = document.createElement("div");
	switchWrapper.className = styles.switchWrapper;

	const candidateBtn: HTMLButtonElement = document.createElement("button");
	candidateBtn.textContent = "Sou Candidato";
	candidateBtn.type = "button";
	candidateBtn.addEventListener("click", onCandidateClick);

	const companyBtn: HTMLButtonElement = document.createElement("button");
	companyBtn.textContent = "Sou Empresa";
	companyBtn.type = "button";
	companyBtn.addEventListener("click", onCompanyClick);

	switchWrapper.appendChild(candidateBtn);
	switchWrapper.appendChild(companyBtn);

	return switchWrapper;
}

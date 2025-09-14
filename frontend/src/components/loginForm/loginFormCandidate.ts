import { store } from "../../Store";
import { candidateDashboard } from "../../pages/candidateDashboard/candidateDashboard";
import { navigateTo } from "../../utils/router";

export function loginFormCandidate(): HTMLElement {
	const form = document.createElement("form");

	const select = document.createElement("select");
	select.name = "candidate";
	select.required = true;

	store.getCandidateList().forEach((candidate) => {
		const option = document.createElement("option");
		option.value = candidate.id.toString();
		option.textContent = candidate.name;
		select.appendChild(option);
	});

	form.appendChild(select);

	const submitBtn = document.createElement("button");
	submitBtn.type = "submit";
	submitBtn.textContent = "Entrar como Candidato";
	form.appendChild(submitBtn);

	form.addEventListener("submit", (e) => {
		e.preventDefault();
		const selectedId = Number(select.value);
		const candidate = store
			.getCandidateList()
			.find((c) => c.id === selectedId);
		if (candidate) {
			navigateTo(candidateDashboard(candidate));
		}
	});

	return form;
}

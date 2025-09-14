import { Job } from "./types/Job";
import { Company } from "./types/Company";
import { Candidate } from "./types/Candidate";

class Store {
	private nextJobId: number;
	private nextCandidateId: number;
	private nextCompanyId: number;
	private candidateList: Candidate[];
	private companyList: Company[];

	constructor() {
		this.nextJobId = 1;
		this.nextCandidateId = 1;
		this.nextCompanyId = 1;
		this.candidateList = [];
		this.companyList = [];
		this.load();
	}

	save(): void {
		localStorage.setItem(
			"store",
			JSON.stringify({
				nextJobId: this.nextJobId,
				nextCandidateId: this.nextCandidateId,
				nextCompanyId: this.nextCompanyId,
				candidateList: this.candidateList,
				companyList: this.companyList,
			})
		);
	}

	load(): void {
		const savedData = localStorage.getItem("store");
		if (!savedData) return;

		const parsed = JSON.parse(savedData);

		this.nextJobId = parsed.nextJobId;
		this.nextCandidateId = parsed.nextCandidateId;
		this.nextCompanyId = parsed.nextCompanyId;

		this.candidateList = parsed.candidateList.map(
			(c: any) =>
				new Candidate(
					c.id,
					c.name,
					c.email,
					c.state,
					c.cep,
					c.description,
					c.cpf,
					c.age,
					c.graduation,
					c.competencies
				)
		);

		this.companyList = parsed.companyList.map((co: any) => {
			const company = new Company(
				co.id,
				co.name,
				co.email,
				co.state,
				co.cep,
				co.description,
				co.cnpj,
				co.country
			);

			if (co.jobList) {
				co.jobList.forEach((j: any) => {
					company.addJob(
						new Job(j.id, j.name, j.description, j.competencies)
					);
				});
			}
			return company;
		});
	}

	addCandidate(candidate: Candidate): boolean {
		this.candidateList.push(candidate);
		this.save();
		return true;
	}

	addCompany(company: Company): boolean {
		this.companyList.push(company);
		this.save();
		return true;
	}

	removeCandidateByIndex(index: number): Candidate {
		let candidate = this.candidateList[index];
		this.candidateList.splice(index, 1);
		this.save();
		return candidate;
	}

	removeCandidateByElement(candidate: Candidate): boolean {
		let index = this.candidateList.indexOf(candidate);
		this.removeCandidateByIndex(index);
		this.save();
		return true;
	}

	removeCompanyByIndex(index: number): Company {
		let company = this.companyList[index];
		this.companyList.splice(index, 1);
		this.save();
		return company;
	}

	removeCompanyByElement(company: Company): boolean {
		let index = this.companyList.indexOf(company);
		this.removeCompanyByIndex(index);
		this.save();
		return true;
	}

	getCandidateList(): Candidate[] {
		return this.candidateList;
	}

	getCompanyList(): Company[] {
		return this.companyList;
	}

	getJobList(): Job[] {
		let list: Job[] = [];

		for (let i: number = 0; i < this.companyList.length; i++) {
			const company = this.companyList[i];
			if (
				company &&
				company.getJobList() &&
				company.getJobList().length > 0
			) {
				company.getJobList().forEach((element) => {
					list.push(element);
				});
			}
		}
		return list;
	}

	getNextJobId(): number {
		return this.nextJobId++;
	}

	getNextCandidateId(): number {
		return this.nextCandidateId++;
	}

	getNextCompanyId(): number {
		return this.nextCompanyId++;
	}
}

// pra manter o singleton entre hot reloads
let storeInstance: Store =
	(import.meta.hot?.data.storeInstance as Store | undefined) || new Store();

if (import.meta.hot) {
	import.meta.hot.dispose((data) => {
		data.storeInstance = storeInstance;
	});
}

export const store: Store = storeInstance;

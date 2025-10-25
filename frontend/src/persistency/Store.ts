import { Candidate } from "../types/Candidate";
import { Company } from "../types/Company";
import { Job } from "../types/Job";
import { LocalStorage } from "./LocalStorage";
import { IdGenerator } from "./IdGenerator";

export class Store {
	private static instance: Store | null = null;

	private candidateList: Candidate[];
	private companyList: Company[];
	private localStorage: LocalStorage;
	private idGenerator: IdGenerator;

	private constructor(localStorage: LocalStorage, idGenerator: IdGenerator) {
		this.localStorage = localStorage;
		this.idGenerator = idGenerator;
		this.candidateList = [];
		this.companyList = [];
		this.load();
	}

	static getInstance(
		localStorage?: LocalStorage,
		idGenerator?: IdGenerator
	): Store {
		if (!Store.instance) {
			if (!localStorage || !idGenerator) {
				throw new Error(
					"Store not initialized. Provide LocalStorage and IdGenerator."
				);
			}
			Store.instance = new Store(localStorage, idGenerator);
		}
		return Store.instance;
	}

	static clearInstance(): void {
		Store.instance = null;
	}

	save(): void {
		this.localStorage.save({
			idGenerator: this.idGenerator,
			candidateList: this.candidateList,
			companyList: this.companyList,
		});
	}

	private load(): void {
		const parsed = this.localStorage.load();
		if (!parsed) return;

		this.candidateList =
			parsed.candidateList?.map(
				(c: any) =>
					new Candidate(
						c.name,
						c.email,
						c.state,
						c.cep,
						c.description,
						c.cpf,
						c.age,
						c.graduation,
						c.competencies,
						c.id
					)
			) ?? [];

		this.companyList =
			parsed.companyList?.map((co: any) => {
				const company = new Company(
					co.name,
					co.email,
					co.state,
					co.cep,
					co.description,
					co.cnpj,
					co.country,
					co.id
				);

				co.jobList?.forEach((j: any) => {
					const job = new Job(
						j.name,
						j.description,
						j.competencies,
						j.id
					);
					company.addJob(job);
				});
				return company;
			}) ?? [];
	}

	addCandidate(candidate: Candidate): boolean {
		candidate.id = this.idGenerator.getNextCandidateId();
		this.candidateList.push(candidate);
		this.save();
		return true;
	}

	addCompany(company: Company): boolean {
		company.id = this.idGenerator.getNextCompanyId();
		this.companyList.push(company);
		this.save();
		return true;
	}

	addJobToCompany(company: Company, job: Job): boolean {
		job.id = this.idGenerator.getNextJobId();
		company.addJob(job);
		this.save();
		return true;
	}

	removeJobByIndexFromCompany(company: Company, index: number): void {
		company.removeJobByIndex(index);
		this.save();
	}

	removeJobByElementFromCompany(company: Company, job: Job): void {
		company.removeJobByElement(job);
		this.save();
	}

	removeCandidateByIndex(index: number): Candidate {
		const candidate = this.candidateList[index];
		this.candidateList.splice(index, 1);
		this.save();
		return candidate;
	}

	removeCandidateByElement(candidate: Candidate): boolean {
		const index = this.candidateList.indexOf(candidate);
		if (index >= 0) this.removeCandidateByIndex(index);
		return true;
	}

	removeCompanyByIndex(index: number): Company {
		const company = this.companyList[index];
		this.companyList.splice(index, 1);
		this.save();
		return company;
	}

	removeCompanyByElement(company: Company): boolean {
		const index = this.companyList.indexOf(company);
		if (index >= 0) this.removeCompanyByIndex(index);
		return true;
	}

	getCandidateList(): Candidate[] {
		return this.candidateList;
	}

	getCompanyList(): Company[] {
		return this.companyList;
	}

	getJobList(): Job[] {
		return this.companyList.flatMap((c: Company) => c.getJobList());
	}
}

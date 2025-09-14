import type { Job } from "./Job";
import { Person } from "./Person";

export class Company extends Person {
	cnpj: string;
	country: string;
	private jobList: Job[];

	constructor(
		id: number,
		name: string,
		email: string,
		state: string,
		cep: string,
		description: string,
		cnpj: string,
		country: string
	) {
		super(id, name, email, state, cep, description);
		this.cnpj = cnpj;
		this.country = country;
		this.jobList = [];
	}

	getJobList(): Job[] {
		return this.jobList;
	}

	addJob(job: Job): boolean {
		this.jobList.push(job);
		return true;
	}

	removeJobByIndex(index: number): Job {
		let job = this.jobList[index];
		this.jobList.splice(index, 1);
		return job;
	}

	removeJobByElement(job: Job): boolean {
		let index = this.jobList.indexOf(job);
		this.removeJobByIndex(index);
		return true;
	}
}

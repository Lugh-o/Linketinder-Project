import type { Address } from "./Address";

export class Job {
	idJob: number;
	idCompany: number;
	name: string;
	description: string;
	competencies: string[];
	address: Address;

	constructor(
		idJob: number,
		idCompany: number,
		name: string,
		description: string,
		competencies: string[],
		address: Address
	) {
		this.idJob = idJob;
		this.idCompany = idCompany;
		this.name = name;
		this.description = description;
		this.competencies = competencies;
		this.address = address;
	}
}

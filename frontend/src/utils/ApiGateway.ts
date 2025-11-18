import { Address } from "../types/Address";
import { Candidate } from "../types/Candidate";
import { Company } from "../types/Company";
import { Job } from "../types/Job";

export class ApiGateway {
	private url: string = "http://localhost:8080/linketinder";

	async getAllCandidates(): Promise<Candidate[]> {
		const endpoint: string = this.url + "/api/v1/candidates";
		const response = await fetch(endpoint);
		const json = await response.json();

		const items = json.data;

		return items.map((item: any) => {
			const address = new Address(
				item.address.state,
				item.address.postalCode,
				item.address.country,
				item.address.city,
				item.address.street
			);

			const competencies = item.competencies.map((c: any) => c.name);

			const birthday = `${item.birthday.year}-${String(
				item.birthday.monthValue
			).padStart(2, "0")}-${String(item.birthday.dayOfMonth).padStart(
				2,
				"0"
			)}`;

			return new Candidate(
				item.idCandidate,
				item.idPerson,
				item.email,
				item.passwd,
				item.description,
				address,
				item.firstName,
				item.lastName,
				item.cpf,
				birthday,
				item.graduation,
				competencies
			);
		});
	}

	async createCandidate(candidate: Candidate): Promise<Candidate> {
		const endpoint: string = this.url + "/api/v1/candidates";

		const body = {
			description: candidate.description,
			passwd: candidate.passwd,
			email: candidate.email,
			firstName: candidate.firstName,
			lastName: candidate.lastName,
			cpf: candidate.cpf,
			birthday: candidate.birthday,
			graduation: candidate.graduation,
			address: {
				state: candidate.address.state,
				postalCode: candidate.address.postalCode,
				country: candidate.address.country,
				city: candidate.address.city,
				street: candidate.address.street,
			},
			competencies: candidate.competencies,
		};

		const response = await fetch(endpoint, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(body),
		});

		const json = await response.json();
		const item = json.data;

		const address = new Address(
			item.address.postalCode,
			item.address.country,
			item.address.state,
			item.address.city,
			item.address.street
		);

		const competencies = item.competencies.map((c: any) => c.name);

		const birthday = `${item.birthday.year}-${String(
			item.birthday.monthValue
		).padStart(2, "0")}-${String(item.birthday.dayOfMonth).padStart(
			2,
			"0"
		)}`;

		return new Candidate(
			item.idCandidate,
			item.idPerson,
			item.email,
			item.passwd,
			item.description,
			address,
			item.firstName,
			item.lastName,
			item.cpf,
			birthday,
			item.graduation,
			competencies
		);
	}

	async getAllCompanies(): Promise<Company[]> {
		const endpoint: string = this.url + "/api/v1/companies";

		const response = await fetch(endpoint);
		const json = await response.json();

		const items = json.data;

		return items.map((item: any) => {
			const address = new Address(
				item.address.postalCode,
				item.address.country,
				item.address.state,
				item.address.city,
				item.address.street
			);

			return new Company(
				item.idPerson,
				item.idCompany,
				item.email,
				item.passwd,
				item.description,
				address,
				item.cnpj,
				item.name
			);
		});
	}

	async createCompany(company: Company): Promise<Company> {
		const endpoint: string = this.url + "/api/v1/companies";

		const body = {
			description: company.description,
			passwd: company.passwd,
			email: company.email,
			name: company.name,
			cnpj: company.cnpj,
			address: {
				state: company.address.state,
				postalCode: company.address.postalCode,
				country: company.address.country,
				city: company.address.city,
				street: company.address.street,
			},
		};

		const response = await fetch(endpoint, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(body),
		});

		const json = await response.json();
		const item = json.data;

		const address = new Address(
			item.address.postalCode,
			item.address.country,
			item.address.state,
			item.address.city,
			item.address.street
		);

		return new Company(
			item.idPerson,
			item.idCompany,
			item.email,
			item.passwd,
			item.description,
			address,
			item.cnpj,
			item.name
		);
	}

	async getAllJobsAsCandidate(): Promise<Job[]> {
		const endpoint: string = this.url + "/api/v1/jobs/candidate";

		const response = await fetch(endpoint, { method: "GET" });
		const json = await response.json();

		return json.data.map((j: any) => {
			const address = new Address(
				j.address.postalCode,
				j.address.country,
				j.address.state,
				j.address.city,
				j.address.street
			);

			const competencies = j.competencies.map((c: any) => c.name);

			return new Job(
				j.idJob,
				j.idCompany,
				j.name,
				j.description,
				competencies,
				address
			);
		});
	}

	async getAllJobsFromCompany(id: number): Promise<Job[]> {
		const endpoint: string = this.url + "/api/v1/jobs/" + id + "/company";

		const response = await fetch(endpoint, { method: "GET" });
		const json = await response.json();

		return json.data.map((j: any) => {
			const address = new Address(
				j.address.postalCode,
				j.address.country,
				j.address.state,
				j.address.city,
				j.address.street
			);

			const competencies = j.competencies.map((c: any) => c.name);

			return new Job(
				j.idJob,
				j.idCompany,
				j.name,
				j.description,
				competencies,
				address
			);
		});
	}

	async createJob(job: Job): Promise<Job> {
		const endpoint: string = this.url + "/api/v1/jobs";

		const body = {
			name: job.name,
			description: job.description,
			idCompany: job.idCompany,
			address: {
				state: job.address.state,
				postalCode: job.address.postalCode,
				country: job.address.country,
				city: job.address.city,
				street: job.address.street,
			},
			competencies: job.competencies,
		};

		const response = await fetch(endpoint, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(body),
		});

		const json = await response.json();
		const d = json.data;

		const address = new Address(
			d.address.postalCode,
			d.address.country,
			d.address.state,
			d.address.city,
			d.address.street
		);

		const competencies = d.competencies.map((c: any) => c.name);

		return new Job(
			d.idJob,
			d.idCompany,
			d.name,
			d.description,
			competencies,
			address
		);
	}
}

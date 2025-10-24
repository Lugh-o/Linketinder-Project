export class IdGenerator {
	private nextJobId: number;
	private nextCandidateId: number;
	private nextCompanyId: number;

	constructor(
		nextJobId?: number,
		nextCandidateId?: number,
		nextCompanyId?: number
	) {
		this.nextJobId = nextJobId ?? 1;
		this.nextCandidateId = nextCandidateId ?? 1;
		this.nextCompanyId = nextCompanyId ?? 1;
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

#include <stdio.h>

int main() {
	//> 68964
	long long r0;
	int r1, r2, r3, r5;

	r0 = 0;
	r1 = 103;
	r2 = 0;
	r3 = 0;
	r5 = 10551339;

	r3 = 1;
	do {
		r2 = 1;
		do {
			r1 = r3 * r2;
			if (r1 == r5) r0 += r3;
			r2++;
		} while (r2 <= r5);

		r3++;
		if (r3 % 1000 == 0) printf(".\n");
	} while (r3 <= r5);

	printf("%lld\n", r0);
}
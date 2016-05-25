#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <pthread.h>
#define N 100000

void swap(int *xp, int *yp) {
	int temp = *xp;
	*xp = *yp;
	*yp = temp;
}
void bubbleSort(int arr[], int n) {
	int i, j;
	for (i = 0; i < n-1; i++)
		for (j = 0; j < n-i-1; j++)
	  		if (arr[j] > arr[j+1])
	swap(&arr[j], &arr[j+1]);
}
void printArray(int arr[], int size)
{
 int i;
 for (i=0; i < size; i++)
 printf("%d ", arr[i]);
 printf("\n");
}

void *firstHalf(void *arg) {
  int *A = (int *) arg;
  bubbleSort(&A[0], N/2);
  pthread_exit(0);
}

void *secondHalf(void *arg) {
  int *A = (int *) arg;
  bubbleSort(&A[N/2], N/2);
  pthread_exit(0);
}


int main() {
 int i, n;
 int* A;
 clock_t start, end;
 double elapsed_time;

 A = (int *)malloc(sizeof(int)*N);
 if (A == NULL) { 
 printf("Fail to malloc\n");
 exit(0);
 }
 for (i=N-1; i>=0; i--)
A[N-1-i] = i;
 start = clock() ;
 // bubbleSort(&A[0], N/2);
 // bubbleSort(&A[N/2], N/2);
 pthread_t t1, t2;
 pthread_create(&t1, NULL, &firstHalf, A);
 pthread_create(&t2, NULL, &secondHalf, A);

 pthread_join(t1, NULL);
 pthread_join(t2, NULL);

 // printArray(&A[0],N);
 printf("Merge!!!\n");

int* temp;
temp = (int *)malloc(sizeof(int)*N);
int x = 0;
int y = N/2;
for(int c = 0; c < N ; c++){
	// printf("%d %d\n",A[x],A[y]);
	if(A[x] < A[y] || y >= N ){
		temp[c] = A[x];
		x++;
	}
	else{
		temp[c] = A[y];
		y++;
	}
}


 // bubbleSort(A, N);
 end = clock();
 // print the last ten elements
 // printArray(&A[N-10], 10);
 // printf("%d",N);
 printArray(&temp[N-10], 10);
  // printArray(&temp[0],N);
 elapsed_time = (end-start)/(double)CLOCKS_PER_SEC;
 printf("elapsed time = %lf\n", elapsed_time);
 return 0;
}




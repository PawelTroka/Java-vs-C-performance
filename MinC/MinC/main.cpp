#include <iostream>
#include <Windows.h>
#include <time.h>
#include <fstream>
#include <string>

using namespace std;

int c_min(int a, int b)
{
	if (a <= b) {
		return a;
	}
	else {
		return b;
	}
}

class Random
{
private:

public:
	Random()
	{
		srand(time(NULL));
	}
	int nextInt()
	{
		return rand();
	}
	int nextInt(int limit)
	{
		return rand() % limit;
	}
};

class Timer
{
private:
	double PCFreq = 0.0;
	__int64 CounterStart = 0;

public:
	Timer()
	{

	}

	void StartCounter()
	{
		LARGE_INTEGER li;
		if (!QueryPerformanceFrequency(&li))
			cout << "QueryPerformanceFrequency failed!\n";

		PCFreq = double(li.QuadPart) / 1000.0;

		QueryPerformanceCounter(&li);
		CounterStart = li.QuadPart;
	}
	double GetCounter()
	{
		LARGE_INTEGER li;
		QueryPerformanceCounter(&li);
		return double(li.QuadPart - CounterStart) / PCFreq;
	}
};

class Benchmark
{
private:
	static const int innerCount = 100000000;//iloœæ porównañ, zalecana bardzo du¿a, 10milionów do 1miliarda
	static const int outerCount = 1;//mo¿e zostaæ 1 i tak ma ma³y sens
	static const int testCount = 100;//iloœæ powtórzeñ testu, aby uzyskaæ jak¹œ statystykê 100 jest w miarê przyzwoite, 1000  by³oby lepsze, ale te¿ chodzi o czas

	int values[innerCount];// = new int[innerCount];
	int randomTab[innerCount];// = new int[];
	Random* random= new Random();

	ofstream* outputFile=new ofstream("wyniki_c.txt", std::ofstream::out);
	Timer* timer = new Timer();

	void generateValues()
	{
		for (int i = 0; i < innerCount; i++) {
			values[i] = random->nextInt();
		}
	}

	void _test()
	{

		int jTest = 0;

		//1. pomiar czasu ile sama petla trwa
		timer->StartCounter();
		for (int j = 0; j < outerCount; j++) {
			for (int i = 0; i < innerCount - 1; i++) {
				jTest++;
				randomTab[i] += jTest;
				//randomTab[i] = random->nextInt();
			}
		}
		double endTime1 = timer->GetCounter();
		cout << "Trwanie petli " << endTime1<<" |";

		int jTest2 = 0;

		timer->StartCounter();
		//2. pomiar czasu ile petla z funkcja trwa
		for (int j = 0; j < outerCount; j++) {
			for (int i = 0; i < innerCount - 1; i++) {
				jTest2++;

				//randomTab[i] = random->nextInt();
				randomTab[i] += c_min(values[i], values[i + 1]);
			}
		}
		double endTime2 = timer->GetCounter();

		cout << "Trwanie petli z funkcja " << endTime2 << " | ";

		double resultTime = endTime2 - endTime1;

		cout << "Rezultat " << resultTime << "\n";
		outputFile->write(to_string(resultTime).c_str(), to_string(resultTime).size());
		outputFile->write("\n", 1);
		// System.out.println(jTest2);
		cout<<randomTab[random->nextInt(innerCount)];

	}




public:
	Benchmark()
	{
		generateValues();
	}

	void Test()
	{
		for (int i = 0; i < testCount; i++)
		{
			_test();
		}
		outputFile->close();
	}
};






int main()
{
	Benchmark* benchmark = new Benchmark();
	benchmark->Test();


}
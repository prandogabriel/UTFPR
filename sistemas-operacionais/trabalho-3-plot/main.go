package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"

	"github.com/go-echarts/go-echarts/v2/charts"
	"github.com/go-echarts/go-echarts/v2/opts"
)

func generateBarItems(threads, time []string, name string) []opts.LineData {
	items := make([]opts.LineData, 0)
	for i := 0; i < len(threads); i++ {
		// items = append(items, opts.LineData{Value: time[i], Name: "batata", Label: &opts.Label{Show: true}})
		items = append(items, opts.LineData{Value: time[i], Name: name, Symbol: "circle", SymbolSize: 10})
	}
	return items
}

func main() {
	file1, err := os.Open("./out_1.txt")
	if err != nil {
		log.Fatal(err)
	}

	file2, err := os.Open("./out_2.txt")
	if err != nil {
		log.Fatal(err)
	}

	defer file1.Close()
	defer file2.Close()

	scanner1 := bufio.NewScanner(file1)
	// optionally, resize scanner1's capacity for lines over 64K, see next example
	sliceThreads1 := make([]string, 1)
	sliceTime1 := make([]string, 1)
	for scanner1.Scan() {
		line := scanner1.Text()
		lineArgs := strings.Fields(line)
		nThreads := lineArgs[0]
		time := lineArgs[1]
		sliceThreads1 = append(sliceThreads1, nThreads)
		sliceTime1 = append(sliceTime1, time)
		fmt.Println(time, nThreads)
	}
	fmt.Println(sliceThreads1)
	fmt.Println(sliceTime1)

	scanner2 := bufio.NewScanner(file2)
	// optionally, resize scanner's capacity for lines over 64K, see next example
	sliceThreads2 := make([]string, 1)
	sliceTime2 := make([]string, 1)
	for scanner2.Scan() {
		line := scanner2.Text()
		lineArgs := strings.Fields(line)
		nThreads := lineArgs[0]
		time := lineArgs[1]
		sliceThreads2 = append(sliceThreads2, nThreads)
		sliceTime2 = append(sliceTime2, time)
		fmt.Println(time, nThreads)
	}
	fmt.Println(sliceThreads2)
	fmt.Println(sliceTime2)

	if err := scanner2.Err(); err != nil {
		log.Fatal(err)
	}

	// create a new bar instance
	bar := charts.NewLine()
	// set some global options like Title/Legend/ToolTip or anything else
	bar.SetGlobalOptions(charts.WithTitleOpts(opts.Title{
		Title: "Tempo X Número de Threads",
	}), charts.WithYAxisOpts(opts.YAxis{
		Name: "Segundos",
	}), charts.WithXAxisOpts(opts.XAxis{
		Name: "Threads",
	}))

	// Put data into instance
	bar.SetXAxis(sliceThreads1).
		AddSeries("Calculo Pi 1", generateBarItems(sliceThreads1, sliceTime1, "Série 1")).SetSeriesOptions(
		charts.WithLabelOpts(
			opts.Label{
				Show: true,
			}),
		charts.WithAreaStyleOpts(
			opts.AreaStyle{
				Opacity: 0.2,
			}),
	)
	bar.SetXAxis(sliceThreads1).
		AddSeries("Calculo Pi 2", generateBarItems(sliceThreads2, sliceTime2, "Série 2")).SetSeriesOptions(
		charts.WithLabelOpts(
			opts.Label{
				Show: true,
			}),
		charts.WithAreaStyleOpts(
			opts.AreaStyle{
				Opacity: 0.2,
			}),
	)
	f, _ := os.Create("bar-graph.html")

	bar.Render(f)
}

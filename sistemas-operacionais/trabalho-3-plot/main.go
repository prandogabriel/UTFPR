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

// generate random data for bar chart
func generateBarItems(threads, time []string) []opts.BarData {
	items := make([]opts.BarData, 0)
	for i := 0; i < len(threads); i++ {
		items = append(items, opts.BarData{Value: time[i], Name: "batata", Label: &opts.Label{Show: true}})
	}
	return items
}

func main() {
	file, err := os.Open("./out.txt")
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	// optionally, resize scanner's capacity for lines over 64K, see next example
	sliceThreads := make([]string, 1)
	sliceTime := make([]string, 1)
	for scanner.Scan() {
		line := scanner.Text()
		lineArgs := strings.Fields(line)
		nThreads := lineArgs[0]
		time := lineArgs[1]
		sliceThreads = append(sliceThreads, nThreads)
		sliceTime = append(sliceTime, time)
		fmt.Println(time, nThreads)
	}
	fmt.Println(sliceThreads)
	fmt.Println(sliceTime)

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}

	// create a new bar instance
	bar := charts.NewBar()
	// set some global options like Title/Legend/ToolTip or anything else
	bar.SetGlobalOptions(charts.WithTitleOpts(opts.Title{
		Title: "Tempo X Número de Threads",
	}), charts.WithYAxisOpts(opts.YAxis{
		Name: "Segundos",
	}), charts.WithXAxisOpts(opts.XAxis{
		Name: "Threads",
	}))

	// Put data into instance
	bar.SetXAxis(sliceThreads).
		AddSeries("Calculo Pi", generateBarItems(sliceThreads, sliceTime))
	f, _ := os.Create("bar-graph.html")

	bar.Render(f)
}

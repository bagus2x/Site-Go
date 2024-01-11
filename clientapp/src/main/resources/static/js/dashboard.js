$(function () {
    const cardColor = config.colors.white;
    const headingColor = config.colors.headingColor;
    const axisColor = config.colors.axisColor;
    const borderColor = config.colors.borderColor;
    const shadeColor = undefined

    // Total Revenue Report Chart - Bar Chart
    // --------------------------------------------------------------------
    const totalRevenueChartEl = document.querySelector('#totalRevenueChart'),
        totalRevenueChartOptions = {
            series: dashboardStat.consultationBars.map((bar) => ({...bar, name: bar.year})),
            chart: {
                height: 300,
                stacked: true,
                type: 'bar',
                toolbar: {show: false}
            },
            plotOptions: {
                bar: {
                    horizontal: false,
                    columnWidth: '33%',
                    borderRadius: 12,
                    startingShape: 'rounded',
                    endingShape: 'rounded'
                }
            },
            colors: [config.colors.primary, config.colors.info],
            dataLabels: {
                enabled: false
            },
            stroke: {
                curve: 'smooth',
                width: 6,
                lineCap: 'round',
                colors: [cardColor]
            },
            legend: {
                show: true,
                horizontalAlign: 'left',
                position: 'top',
                markers: {
                    height: 8,
                    width: 8,
                    radius: 12,
                    offsetX: -3
                },
                labels: {
                    colors: axisColor
                },
                itemMargin: {
                    horizontal: 10
                }
            },
            grid: {
                borderColor: borderColor,
                padding: {
                    top: 0,
                    bottom: -8,
                    left: 20,
                    right: 20
                }
            },
            xaxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                labels: {
                    style: {
                        fontSize: '13px',
                        colors: axisColor
                    }
                },
                axisTicks: {
                    show: false
                },
                axisBorder: {
                    show: false
                }
            },
            yaxis: {
                labels: {
                    style: {
                        fontSize: '13px',
                        colors: axisColor
                    }
                }
            },
            responsive: [
                {
                    breakpoint: 1700,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '32%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 1580,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '35%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 1440,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '42%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 1300,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '48%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 1200,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '40%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 1040,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 11,
                                columnWidth: '48%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 991,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '30%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 840,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '35%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 768,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '28%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 640,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '32%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 576,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '37%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 480,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '45%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 420,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '52%'
                            }
                        }
                    }
                },
                {
                    breakpoint: 380,
                    options: {
                        plotOptions: {
                            bar: {
                                borderRadius: 10,
                                columnWidth: '60%'
                            }
                        }
                    }
                }
            ],
            states: {
                hover: {
                    filter: {
                        type: 'none'
                    }
                },
                active: {
                    filter: {
                        type: 'none'
                    }
                }
            }
        };
    if (typeof totalRevenueChartEl !== undefined && totalRevenueChartEl !== null) {
        const totalRevenueChart = new ApexCharts(totalRevenueChartEl, totalRevenueChartOptions);
        totalRevenueChart.render();
    }

    // Growth Chart - Radial Bar Chart
    // --------------------------------------------------------------------
    const growth = ((dashboardStat.consultationGrowth.numberOfConsultationsOnThisYear - dashboardStat.consultationGrowth.numberOfConsultationsOnLastYear) / dashboardStat.consultationGrowth.numberOfConsultations) * 100
    const growthChartEl = document.querySelector('#growthChart'),
        growthChartOptions = {
            series: [Math.round(growth * 100) / 100],
            labels: ['Growth'],
            chart: {
                height: 240,
                type: 'radialBar'
            },
            plotOptions: {
                radialBar: {
                    size: 150,
                    offsetY: 10,
                    startAngle: -150,
                    endAngle: 150,
                    hollow: {
                        size: '55%'
                    },
                    track: {
                        background: cardColor,
                        strokeWidth: '100%'
                    },
                    dataLabels: {
                        name: {
                            offsetY: 15,
                            color: headingColor,
                            fontSize: '15px',
                            fontWeight: '600',
                            fontFamily: 'Public Sans'
                        },
                        value: {
                            offsetY: -25,
                            color: headingColor,
                            fontSize: '22px',
                            fontWeight: '500',
                            fontFamily: 'Public Sans'
                        }
                    }
                }
            },
            colors: [config.colors.primary],
            fill: {
                type: 'gradient',
                gradient: {
                    shade: 'dark',
                    shadeIntensity: 0.5,
                    gradientToColors: [config.colors.primary],
                    inverseColors: true,
                    opacityFrom: 1,
                    opacityTo: 0.6,
                    stops: [30, 70, 100]
                }
            },
            stroke: {
                dashArray: 5
            },
            grid: {
                padding: {
                    top: -35,
                    bottom: -10
                }
            },
            states: {
                hover: {
                    filter: {
                        type: 'none'
                    }
                },
                active: {
                    filter: {
                        type: 'none'
                    }
                }
            }
        };
    if (typeof growthChartEl !== undefined && growthChartEl !== null) {
        const growthChart = new ApexCharts(growthChartEl, growthChartOptions);
        growthChart.render();
    }


    // Income Chart - Area chart
    // --------------------------------------------------------------------
    const incomeChartEl = document.querySelector('#incomeChart'),
        incomeChartConfig = {
            series: [
                {
                    data: [24, 21, 30, 22, 42, 26, 35, 29]
                }
            ],
            chart: {
                height: 215,
                parentHeightOffset: 0,
                parentWidthOffset: 0,
                toolbar: {
                    show: false
                },
                type: 'area'
            },
            dataLabels: {
                enabled: false
            },
            stroke: {
                width: 2,
                curve: 'smooth'
            },
            legend: {
                show: false
            },
            markers: {
                size: 6,
                colors: 'transparent',
                strokeColors: 'transparent',
                strokeWidth: 4,
                discrete: [
                    {
                        fillColor: config.colors.white,
                        seriesIndex: 0,
                        dataPointIndex: 7,
                        strokeColor: config.colors.primary,
                        strokeWidth: 2,
                        size: 6,
                        radius: 8
                    }
                ],
                hover: {
                    size: 7
                }
            },
            colors: [config.colors.primary],
            fill: {
                type: 'gradient',
                gradient: {
                    shade: shadeColor,
                    shadeIntensity: 0.6,
                    opacityFrom: 0.5,
                    opacityTo: 0.25,
                    stops: [0, 95, 100]
                }
            },
            grid: {
                borderColor: borderColor,
                strokeDashArray: 3,
                padding: {
                    top: -20,
                    bottom: -8,
                    left: -10,
                    right: 8
                }
            },
            xaxis: {
                categories: ['', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul'],
                axisBorder: {
                    show: false
                },
                axisTicks: {
                    show: false
                },
                labels: {
                    show: true,
                    style: {
                        fontSize: '13px',
                        colors: axisColor
                    }
                }
            },
            yaxis: {
                labels: {
                    show: false
                },
                min: 10,
                max: 50,
                tickAmount: 4
            }
        };
});

$(function () {
    let chartColors, tooltipColors, borderColor;
    const horizontalBarChart = document.querySelector("#horizontalBarChart");

    if (horizontalBarChart) {
        const horizontalBarConfig = {
            chart: {
                height: 270,
                type: "bar",
                toolbar: {show: false},
            },
            plotOptions: {
                bar: {
                    horizontal: true,
                    barHeight: "70%",
                    distributed: true,
                    startingShape: "rounded",
                    borderRadius: 7,
                },
            },
            grid: {
                strokeDashArray: 10,
                borderColor: borderColor,
                xaxis: {lines: {show: true}},
                yaxis: {lines: {show: false}},
                padding: {top: -35, bottom: -12},
            },
            colors: [
                config.colors.success,
                config.colors.info,
                config.colors.primary,
                config.colors.danger,
                config.colors.warning,
            ],
            dataLabels: {
                enabled: true,
                style: {
                    colors: ["#fff"],
                    fontWeight: 200,
                    fontSize: "13px",
                    fontFamily: "Public Sans",
                },
                formatter: function (value, {dataPointIndex}) {
                    return horizontalBarConfig.labels[dataPointIndex];
                },
                offsetX: 0,
                dropShadow: {enabled: false},
            },
            labels: ["Done", "Scheduled", "Validated", "Rejected", "Waiting"],
            series: [{
                data: [
                    (dashboardStat.consultationCounter.numberOfDone / dashboardStat.consultationCounter.numberOfConsultations) * 100,
                    (dashboardStat.consultationCounter.numberOfScheduled / dashboardStat.consultationCounter.numberOfConsultations) * 100,
                    (dashboardStat.consultationCounter.numberOfValidated / dashboardStat.consultationCounter.numberOfConsultations) * 100,
                    (dashboardStat.consultationCounter.numberOfRejected / dashboardStat.consultationCounter.numberOfConsultations) * 100,
                    (dashboardStat.consultationCounter.numberOfWaiting / dashboardStat.consultationCounter.numberOfConsultations) * 100,
                ],
            }],
            xaxis: {
                categories: ["5", "4", "3", "2", "1"],
                axisBorder: {show: false},
                axisTicks: {show: false},
                labels: {
                    style: {
                        colors: chartColors,
                        fontSize: "13px",
                    },
                    formatter: function (value) {
                        return value + "%";
                    },
                },
            },
            yaxis: {
                max: 100,
                labels: {
                    style: {
                        colors: [chartColors],
                        fontFamily: "Public Sans",
                        fontSize: "13px",
                    },
                },
            },
            tooltip: {
                enabled: true,
                style: {fontSize: "12px"},
                onDatasetHover: {highlightDataSeries: false},
                custom: function ({series, seriesIndex, dataPointIndex}) {
                    return '<div class="px-3 py-2"><span>' + series[seriesIndex][dataPointIndex] + "%</span></div>";
                },
            },
            legend: {show: false},
        };

        new ApexCharts(horizontalBarChart, horizontalBarConfig).render();
    }

    const progressCharts = document.querySelectorAll(".chart-progress");

    if (progressCharts) {
        progressCharts.forEach(function (chart) {
            const color = config.colors[chart.dataset.color];
            const seriesValue = chart.dataset.series;
            const progressVariant = chart.dataset.progress_variant;

            const radialBarConfig = {
                chart: {
                    height: progressVariant === "true" ? 58 : 55,
                    width: progressVariant === "true" ? 58 : 45,
                    type: "radialBar",
                },
                plotOptions: {
                    radialBar: {
                        hollow: {size: progressVariant === "true" ? "45%" : "25%"},
                        dataLabels: {
                            show: progressVariant === "true",
                            value: {
                                offsetY: -10,
                                fontSize: "15px",
                                fontWeight: 500,
                                fontFamily: "Public Sans",
                                color: tooltipColors,
                            },
                        },
                        track: {background: config.colors_label.secondary},
                    },
                },
                stroke: {lineCap: "round"},
                colors: [color],
                grid: {
                    padding: {
                        top: progressVariant === "true" ? -12 : -15,
                        bottom: progressVariant === "true" ? -17 : -15,
                        left: progressVariant === "true" ? -17 : -5,
                        right: -15,
                    },
                },
                series: [seriesValue],
                labels: progressVariant === "true" ? [""] : ["Progress"],
            };

            new ApexCharts(chart, radialBarConfig).render();
        });
    }
});


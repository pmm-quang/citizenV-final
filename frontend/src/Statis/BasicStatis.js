import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Form from 'react-bootstrap/Form';
import './BasicStatis.css'
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import { Bar, Line, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js/auto';
import { CategoryScale } from 'chart.js';
import { AiFillCaretRight } from 'react-icons/ai'
import { Button } from 'react-bootstrap';

ChartJS.register(CategoryScale);
ChartJS.register(ArcElement, Tooltip, Legend);

function BasicStatis() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.info.username;
    const role = user_account.info.role;
    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const [option, setOption] = useState();
    const [showAge, setShowAge] = useState(false);
    const [showChartAge, setShowChartAge] = useState(false);
    const [showChart, setShowChart] = useState(false);
    const [dataStatic, setDataStatic] = useState([]);
    const [start, setStart] = useState('')
    const [end, setEnd] = useState('')
    const [dataAgeStatic, setDataAgeStatic] = useState([]);
    const [dataChart, setDataChart] = useState({
        labels: [],
        datasets: [
            {
                label: 'Tổng dân số',
                data: [],
            },
        ],
    });

    let startYear, endYear;

    const GetDataStatic = async (code) => {
        setDataStatic([])
        setDataAgeStatic([])
        setShowAge(false)
        setShowChartAge(false)
        setShowChart(true)
        let response;
        setOption(code)
        if (code === 'region') {
            response = await axios.get("http://localhost:8080/api/v1/statistics/population/region", config)
        } else {
            response = await axios.get("http://localhost:8080/api/v1/statistics/population/citizen?property=" + code, config)
        }
        setOption(code)
        setDataStatic(response.data)
        setDataChart({
            labels: response.data.map(item => (item.name === '') ? "Không có" : item.name),
            datasets: [
                {
                    label: 'Tổng dân số',
                    data: response.data.map(item => item.population),
                },
            ],
        })
        console.log(dataChart)
    }

    const GetDataAgeStatic = async (start, end) => {
        setDataStatic([])
        setDataAgeStatic([])
        setOption("ageGroup")
        setShowChart(false)
        const year = end - start;
        const response = await axios.get("http://localhost:8080/api/v1/statistics/population/citizen/age-group?year=" + year + "&startYear=" + start + "&endYear=" + end, config)
        setDataAgeStatic(response.data)
        setDataChart({
            labels: response.data.map(item => item.year),
            datasets: [
                {
                    label: "Dưới độ tuổi lao động",
                    data: response.data.map(item => item.ageGroupPopulation[0].population)
                },
                {
                    label: "Trong độ tuổi lao động",
                    data: response.data.map(item => item.ageGroupPopulation[1].population)
                },
                {
                    label: "Trên độ tuổi lao động",
                    data: response.data.map(item => item.ageGroupPopulation[2].population)
                }
            ]
        })
        console.log(dataChart)
    }


    useEffect(() => {
    }, [])

    const listStatics = dataStatic.map((post, index) =>
        <tr key={index}>
            <td>{(post.name === '') ? "Không có" : post.name}</td>
            <td>{post.population}</td>
        </tr>
    )

    const listAgeGroupStatics = dataAgeStatic.map((post, index) =>
        <tr key={index}>
            <td>{post.year}</td>
            <td>{post.ageGroupPopulation[0].population}</td>
            <td>{post.ageGroupPopulation[1].population}</td>
            <td>{post.ageGroupPopulation[2].population}</td>
        </tr>
    )

    const TreeUnits = () => {
        return (
            <div className="flex_citizen">
                <div className="title">Các thuộc tính thống kê</div>
                <div className='listProvinces'>
                    <div className="optionStatic" onClick={() => {
                        GetDataStatic('sex')
                    }}><AiFillCaretRight /> 1. Giới tính</div>
                    <div className="optionStatic" onClick={() => {
                        GetDataStatic('maritalStatus')
                    }}><AiFillCaretRight /> 2. Tình trạng hôn nhân</div>
                    <div className="optionStatic" onClick={() => {
                        GetDataStatic('bloodType')
                    }}><AiFillCaretRight /> 3. Nhóm máu</div>
                    <div className="optionStatic" onClick={() => {
                        GetDataStatic('ethnicity')
                    }}><AiFillCaretRight /> 4. Dân tộc</div>
                    <div className="optionStatic" onClick={() => {
                        GetDataStatic('religion')
                    }}><AiFillCaretRight /> 5. Tôn giáo</div>
                    <div className="optionStatic" onClick={() => {
                        GetDataStatic('otherNationality')
                    }}><AiFillCaretRight /> 6. Quốc tịch khác</div>
                    <div className="optionStatic" onClick={() => {
                        setShowAge(true)
                        setShowChart(false)
                    }
                    }><AiFillCaretRight /> 7. Nhóm tuổi</div>
                </div>

            </div>
        )
    }

    const TableStatic = () => {
        return (
            <div>
                <div className='titleStatic'>BẢNG THỐNG KÊ TỔNG DÂN SỐ</div>
                <Table striped bordered hover size="sm" className="tableResidentialInHamlet">
                    <thead>
                        <tr>
                            <th>Thuộc tính</th>
                            <th>Số lượng thống kê</th>
                        </tr>
                    </thead>
                    <tbody>
                        {listStatics}
                    </tbody>
                </Table>
            </div>
        )
    }

    const TableAgeStatic = () => {
        return (
            <div>
                <div className='titleStatic'>BẢNG THỐNG KÊ TỔNG DÂN SỐ</div>
                <Table striped bordered hover size="sm" className="tableResidentialInHamlet">
                    <thead>
                        <tr>
                            <th>Năm</th>
                            <th>Dưới độ tuổi lao động</th>
                            <th>Trong độ tuổi lao động</th>
                            <th>Trên độ tuổi lao động</th>
                        </tr>
                    </thead>
                    <tbody>
                        {listAgeGroupStatics}
                    </tbody>
                </Table>
            </div>
        )
    }

    const SelectYear = () => {
        return (
            <div>
                <div className="titleSelectYear">THỐNG KÊ CÁC NHÓM TUỔI TRONG KHOẢNG THỜI GIAN</div>
                <div className="flex-year">
                    <Form.Group className="formInput">
                        <Form.Label style={{ width: '150px', marginTop: '6px' }}>1*. Năm bắt đầu</Form.Label>
                        <Form.Control type="text" style={{ width: '200px' }} value={start} onChange={(e) => {
                            setStart(e.target.value)
                            setShowChartAge(false)
                        }} />
                    </Form.Group>
                    <Form.Group className="formInput">
                        <Form.Label style={{ width: '150px', marginTop: '6px' }}>2*. Năm kết thúc</Form.Label>
                        <Form.Control type="text" style={{ width: '200px' }} value={end} onChange={(e) => {
                            setEnd(e.target.value)
                            setShowChartAge(false)
                        }} />
                    </Form.Group>
                    <Form.Group style={{ marginLeft: '30px' }}>
                        <Button onClick={() => {
                            GetDataAgeStatic(start, end)
                            setShowChartAge(true)
                        }}> Xác nhận </Button>
                    </Form.Group>
                </div>
            </div>
        )
    }

    const SexStatic = () => {
        return (
            <div className="flexStatic">
                <TreeUnits className="flex_first" />
                <div className="flex_second">
                    {(showAge) ? SelectYear() : null}
                    <div className="chartStatic">
                        {(showChart || showChartAge) ? <div className='titleStatic'>BIỂU ĐỒ THỂ HIỆN CƠ CẤU</div> : null}
                        {(showChart) ? <Pie data={dataChart} className='chart' /> : null}
                        {(option === 'region') ? <Bar data={dataChart} className='chart' /> : null}
                    </div>
                    <div className="chartLine">
                        {(showChartAge) ? <Line data={dataChart} /> : null}
                    </div>
                    <div className='flex_second'>
                        {(showChartAge) ? <TableAgeStatic /> : null}
                        {(showChart) ? <TableStatic /> : null}
                    </div>
                </div>
            </div>
        )
    }

    return (
        <div>
            <NavbarPage />
            {SexStatic()}
        </div>
    );
}

export default BasicStatis;

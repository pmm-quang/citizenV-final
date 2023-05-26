import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Form from 'react-bootstrap/Form';
import './BasicStatis.css'
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import { Bar, Line, Pie } from 'react-chartjs-2';
import { AiFillCaretRight } from 'react-icons/ai'
import { Button } from 'react-bootstrap';
import Select from 'react-select'
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Colors,
    ArcElement
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
    Colors,
    ArcElement
);

function BasicStatis() {
    const user_account = JSON.parse(localStorage.getItem("user"));
    const user = user_account.username;
    const role = user_account.role;
    const config = {
        headers: {
            Authorization: `Bearer ${user_account.accessToken}`
        },
    };

    const [listProvinces, setListProvinces] = useState([])
    const [option, setOption] = useState();
    const [showAge, setShowAge] = useState(false);
    const [showMultiStatic, setShowMultiStatic] = useState(false);
    const [showTableMultiStatic, setShowTableMultiStatic] = useState(false);
    const [showChartAge, setShowChartAge] = useState(false);
    const [showChart, setShowChart] = useState(false);
    const [dataStatic, setDataStatic] = useState([]);
    const [start, setStart] = useState('')
    const [end, setEnd] = useState('')
    const [dataAgeStatic, setDataAgeStatic] = useState([]);
    const [value, setValue] = useState([])
    const [provinceCodes, setProvinceCodes] = useState([])
    const [optionCodes, setOptionCodes] = useState([])
    const [dataMultiStatic, setDataMultiStatic] = useState([])
    const [dataChart, setDataChart] = useState({
        labels: [],
        datasets: [
            {
                label: 'Tổng dân số',
                data: [],
            },
        ],
    });

    const [listOption, setListOption] = useState([
        { value: 'sex', label: "Giới tính" },
        { value: 'maritalStatus', label: "Tình trạng hôn nhân" },
        { value: 'bloodType', label: "Nhóm máu" },
        { value: 'ethnicity', label: "Dân tộc" },
        { value: 'religion', label: "Tôn giáo" },
        { value: 'otherNationality', label: "Quốc tịch khác" }
    ])
    const fetchProvince = async () => {
        try {
            const response = await axios('http://localhost:8080/api/v1/province/', config);
            setListProvinces(response.data);
        } catch (err) {
            console.error(err);
        }
    };

    const checkAccount = (listProvinces) => {
        const updatedProvinces = listProvinces.map((province) => ({
            ...province,
            checked: false,
        }));
        setListProvinces(updatedProvinces);
    };

    const GetDataStatic = async (code) => {
        setShowMultiStatic(false)
        setShowTableMultiStatic(false)
        setDataStatic([])
        setDataAgeStatic([])
        setShowAge(false)
        setShowChartAge(false)
        setShowChart(true)
        setOption(code)
        let response
        setOption(code)
        if (role === 'A1') {
            const data = {
                "division": "province",
                "codes": null,
                "properties": [
                    code
                ]
            }
            console.log(data)
            response = await axios.get("http://localhost:8080/api/v1/statistics/population/citizen?property=" + code, config)
            setDataStatic(response.data)
            console.log(response.data)
            setDataChart({
                labels: response.data.map(item => (item.name === '') ? "Không có" : item.name),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data.map(item => item.population),
                    },
                ],
            })
        }
        else if (role === 'A2') {
            const data = {
                "division": "province",
                "codes": [
                    user
                ],
                "properties": [
                    code
                ]
            }
            console.log(data)
            response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataStatic(response.data[0].details)
            setDataChart({
                labels: response.data[0].details.map(item => (item[0] === null) ? "Không có" : item[0]),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map(item => item[1]),
                    },
                ],
            })
        } else if (role === 'A3') {
            const data = {
                "division": "district",
                "codes": [
                    user
                ],
                "properties": [
                    code
                ]
            }
            console.log(data)
            response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataStatic(response.data[0].details)
            setDataChart({
                labels: response.data[0].details.map(item => (item[0] === null) ? "Không có" : item[0]),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map(item => item[1]),
                    },
                ],
            })
        } else if (role === 'B1') {
            const data = {
                "division": "ward",
                "codes": [
                    user
                ],
                "properties": [
                    code
                ]
            }
            console.log(data)
            response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataStatic(response.data[0].details)
            setDataChart({
                labels: response.data[0].details.map(item => (item[0] === null) ? "Không có" : item[0]),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map(item => item[1]),
                    },
                ],
            })
        } else if (role === 'B2') {
            const data = {
                "division": "hamlet",
                "codes": [
                    user
                ],
                "properties": [
                    code
                ]
            }
            console.log(data)
            response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataStatic(response.data[0].details)
            setDataChart({
                labels: response.data[0].details.map(item => (item[0] === null) ? "Không có" : item[0]),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map(item => item[1]),
                    },
                ],
            })
        }
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
        fetchProvince()
    }, [])

    const optionStaticProvinces = listProvinces.map((province) => ({
        value: province.code,
        label: province.name
    })
    )

    const listStaticsDivison = dataStatic.map((post, index) =>
        <tr key={index}>
            <td>{(post[0] === null) ? "Không có" : post[0]}</td>
            <td>{post[1]}</td>
        </tr>
    )

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

    const listDataMultiStatic = dataMultiStatic.map((dataList, index) =>
        dataList.details.map((data) =>
            <tr key={index}>
                <td>{dataList.name}</td>
                {data.map((value) =>
                    <td>{value}</td>)}
            </tr>)
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
                        setShowMultiStatic(false)
                        setShowTableMultiStatic(false)
                    }
                    }><AiFillCaretRight /> 7. Nhóm tuổi</div>
                    <div className="optionStatic" onClick={() => {
                        setShowMultiStatic(true)
                        setShowChart(false)
                        setShowChartAge(false)
                        setShowAge(false)
                    }
                    }><AiFillCaretRight /> 8. Thuộc tính kết hợp</div>
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
                        {(role === 'A1') ? listStatics : listStaticsDivison}
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

    const TableMultiStatic = () => {
        const optionList = optionCodes.map((option) => (option.label))
        console.log(optionList)
        return (
            <div>
                <div className='titleStatic'>BẢNG THỐNG KÊ TỔNG DÂN SỐ</div>
                <Table striped bordered hover size="sm" className="tableResidentialInHamlet">
                    <thead>
                        <tr>
                            <th>Đơn vị hành chính</th>
                            {optionList.map((option, index) =>
                                <th key={index}>{option}</th>
                            )}
                            <th>Tổng dân số</th>
                        </tr>
                    </thead>
                    <tbody>
                        {listDataMultiStatic}
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

    const GetDataToStatic = async () => {
        setShowTableMultiStatic(true)
        const provinceList = provinceCodes.map((province) => (province.value))
        console.log(provinceList)
        const optionList = optionCodes.map((option) => (option.value))
        console.log(optionList)
        if (role === 'A1') {
            const data = {
                "division": "province",
                "codes": provinceList,
                "properties": optionList
            }
            console.log(data)
            const response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataMultiStatic(response.data)
            console.log(response.data)
        }
    }

    const SelectOption = () => {
        return (
            <div>
                <div className="titleSelectYear">THỐNG KÊ TÙY CHỌN</div>
                <div>
                    <Form.Group className="formInput" style={{ marginBottom: '20px' }}>
                        <Form.Label style={{ width: '200px', marginTop: '6px' }}>1*. Đơn vị hành chính</Form.Label>
                        <Select className="selectMultiOption" placeholder="Chọn danh sách đơn vị hành chính cần thống kê" options={optionStaticProvinces} isMulti onChange={(e) => {
                            setProvinceCodes(e)
                            setShowTableMultiStatic(false)
                        }} />
                    </Form.Group>
                    <Form.Group className="formInput">
                        <Form.Label style={{ width: '200px', marginTop: '6px' }}>2*. Thuộc tính thống kê</Form.Label>
                        <Select className="selectMultiOption" placeholder="Chọn danh sách thuộc tính cần thống kê" options={listOption} isMulti onChange={(e) => {
                            setOptionCodes(e)
                            setShowTableMultiStatic(false)
                        }} />
                    </Form.Group>
                    <Form.Group style={{ marginLeft: '30px', marginTop: '20px' }}>
                        <Button onClick={() => {
                            GetDataToStatic()
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
                    {(showMultiStatic) ? SelectOption() : null}
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
                        {(showTableMultiStatic) ? <TableMultiStatic /> : null}
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

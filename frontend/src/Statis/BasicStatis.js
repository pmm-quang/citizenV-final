import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import NavbarPage from '../Navbar/NavbarPage.js';
import Form from 'react-bootstrap/Form';
import './BasicStatis.css'
import Table from 'react-bootstrap/Table';
import axios from 'axios';
import { Bar, Doughnut, Line, Pie } from 'react-chartjs-2';
import { AiFillCaretRight } from 'react-icons/ai'
import { Button } from 'react-bootstrap';
import Select from 'react-select'
import { Modal } from 'react-bootstrap';
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
    ArcElement,
    BarElement
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
    BarElement,
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

    const [listDivisions, setListDivisions] = useState([])
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
    const [showBasicStatis, setShowBasicStatis] = useState(false)
    const [provinceCodes, setProvinceCodes] = useState([])
    const [optionCodes, setOptionCodes] = useState([])
    const [dataMultiStatic, setDataMultiStatic] = useState([])
    const [optionList, setOptionList] = useState([])
    const [firstSelectOption, setFirstSelectOption] = useState(false)
    const [secondSelectOption, setSecondSelectOption] = useState(false)
    const [checkedAgeGroup, setCheckedAgeGroup] = useState(false)
    const [titlePage, setTitlePage] = useState('')
    const [showWarningEndYear, setShowWarningEndYear] = useState(false)
    const [heightScreen, setHeightScreen] = useState(window.screen.height)
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
        { value: 'sex', label: "Giới tính", checked: false },
        { value: 'maritalStatus', label: "Tình trạng hôn nhân", checked: false },
        { value: 'bloodType', label: "Nhóm máu", checked: false },
        { value: 'ethnicity', label: "Dân tộc", checked: false },
        { value: 'religion', label: "Tôn giáo", checked: false },
        { value: 'otherNationality', label: "Quốc tịch khác", checked: false },
        { value: 'job', label: "Nghề nghiệp", checked: false },
        { value: 'educationLevel', label: "Trình độ văn hóa", checked: false }
    ])

    const fetchProvince = async () => {
        if (role === 'A1') {
            try {
                const response = await axios('http://localhost:8080/api/v1/province/', config);
                setListDivisions(response.data);
            } catch (err) {
                console.error(err);
            }
        } else if (role === 'A2') {
            try {
                const response = await axios('http://localhost:8080/api/v1/district/by-province/' + user, config);
                setListDivisions(response.data);
            } catch (err) {
                console.error(err);
            }
        } else if (role === 'A3') {
            try {
                const response = await axios('http://localhost:8080/api/v1/ward/by-district/' + user, config);
                setListDivisions(response.data);
            } catch (err) {
                console.error(err);
            }
        } else if (role === 'B1') {
            try {
                const response = await axios('http://localhost:8080/api/v1/hamlet/by-ward/' + user, config);
                setListDivisions(response.data);
            } catch (err) {
                console.error(err);
            }
        } else if (role === 'B2') {
            setProvinceCodes([user])
        }
    };

    const GetDataStatic = async (code) => {
        setHeightScreen(window.screen.height)
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
            response = await axios.get("http://localhost:8080/api/v1/statistics/population/citizen?property=" + code, config)
            setDataStatic(response.data)
            console.log(response.data)
            setDataChart({
                labels: response.data.map(item => (item.name === '') ? "Không có" : item.name),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data.map(item => item.population),
                        strokeColor: "rgba(220,220,220,0.8)",
                        highlightFill: "rgba(220,220,220,0.75)",
                        highlightStroke: "rgba(220,220,220,1)",
                        borderWidth: 1
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
                labels: response.data[0].details.map(item => (listDataOption(item, code) === null) ? "Không có" : listDataOption(item, code)),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map((item) => item.population),
                        borderWidth: 1
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
                labels: response.data[0].details.map(item => (listDataOption(item, code) === null) ? "Không có" : listDataOption(item, code)),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map((item) => item.population),
                        strokeColor: "rgba(220,220,220,0.8)",
                        highlightFill: "rgba(220,220,220,0.75)",
                        highlightStroke: "rgba(220,220,220,1)",
                        borderWidth: 1
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
                labels: response.data[0].details.map(item => (listDataOption(item, code) === null) ? "Không có" : listDataOption(item, code)),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map((item) => item.population),
                        borderWidth: 1
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
                labels: response.data[0].details.map(item => (listDataOption(item, code) === null) ? "Không có" : listDataOption(item, code)),
                datasets: [
                    {
                        label: 'Tổng dân số',
                        data: response.data[0].details.map((item) => item.population),
                        strokeColor: "rgba(220,220,220,0.8)",
                        highlightFill: "rgba(220,220,220,0.75)",
                        highlightStroke: "rgba(220,220,220,1)",
                        borderWidth: 1
                    },
                ],
            })
        }
    }

    const GetDataAgeStatic = async (start, end) => {
        setHeightScreen(window.screen.height)
        if (end > 2023) {
            setShowWarningEndYear(true)
        } else {
            setShowChartAge(true)
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
    }


    useEffect(() => {
        fetchProvince()
    }, [])

    const listDataOption = (data, option) => {
        if (option === 'sex') return data.sex
        else if (option === 'maritalStatus') return data.maritalStatus
        else if (option === 'bloodType') return data.bloodType
        else if (option === 'otherNationality') return data.otherNationality
        else if (option === 'religion') return data.religion
        else if (option === 'ethnicity') return data.ethnicity
        else if (option === 'name') return data.name
        else if (option === 'population') return data.population
    }


    const optionStaticProvinces = listDivisions.map((division) => ({
        value: division.code,
        label: division.name
    })
    )

    const listStaticsDivison = dataStatic.map((post, index) =>
        <tr key={index}>
            <td>{(listDataOption(post, option) === null) ? "Không có" : listDataOption(post, option)}</td>
            <td>{post.population}</td>
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
                {optionList.map((option) => <td>{listDataOption(data, option)}</td>)}
                <td>{data.population}</td>
            </tr>)
    )

    const listOptionBasicStatic = listOption.map((option) =>
        <div className="buttonChildOption" style={(option.checked) ? { backgroundColor: 'yellow' } : null} onClick={() => {
            GetDataStatic(option.value)
            option.checked = true
            setTitlePage(" THEO " + (option.label).toUpperCase())
            listOption.map((otherOption) => {
                if (otherOption.value !== option.value) otherOption.checked = false
            }
            )
            setCheckedAgeGroup(false)
        }}>{option.label}</div>
    )

    const BasicStatis = () => {
        return (
            <div>
                {listOptionBasicStatic}
                <div className="buttonChildOption" style={(checkedAgeGroup) ? { backgroundColor: 'yellow' } : null} onClick={() => {
                    setShowAge(true)
                    setShowChart(false)
                    setShowMultiStatic(false)
                    setShowTableMultiStatic(false)
                    setCheckedAgeGroup(true)
                    setTitlePage(" THEO NHÓM TUỔI")
                    listOption.map((option) => {
                        option.checked = false
                    })
                }
                }>Nhóm tuổi</div>
            </div>
        )
    }
    const TreeUnits = () => {
        return (
            <div className="statisOption" style={{ height: heightScreen }}>
                <div className="buttonOption" style={(firstSelectOption) ? { backgroundColor: 'yellow' } : null} onClick={() => {
                    setFirstSelectOption(!firstSelectOption)
                    setSecondSelectOption(false)
                    setShowMultiStatic(false)
                    setShowTableMultiStatic(false)
                    setCheckedAgeGroup(false)
                    listOption.map((option) => {
                        option.checked = false
                    })
                    setShowChart(false)
                    setShowChartAge(false)
                    setShowAge(false)
                    setShowBasicStatis(false)
                }}><div className="titleOption">Thống kê cơ bản</div></div>
                {(firstSelectOption) ? <BasicStatis /> : null}
                <div className="buttonOption" style={(secondSelectOption) ? { backgroundColor: 'yellow' } : null} onClick={() => {
                    setShowMultiStatic(true)
                    setShowChart(false)
                    setShowChartAge(false)
                    setShowAge(false)
                    setShowBasicStatis(false)
                    setSecondSelectOption(!secondSelectOption)
                    setFirstSelectOption(false)
                    setCheckedAgeGroup(false)
                    listOption.map((option) => {
                        option.checked = false
                    })
                }}><div className="titleOption">Thống kê tùy chọn</div></div>
                <div className="noteStatis">⁕ Thống kê cơ bản là những thống kê về dân số dựa trên một thuộc tính cơ bản trong bảng thông tin người dân (giới tính, độ tuổi, nhóm máu,...) trên phạm vi toàn bộ đơn vị</div>
                <div className="noteStatis">⁕ Thống kê tùy chọn là những thống kê có thể kết hợp nhiều thuộc tính cơ bản và nhiều đơn vị hành chính (nằm trong khu vực quản lý) để có cái nhìn tổng quan hơn về các vấn đề về dân số trong khu vực</div>
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
                <Table striped bordered hover size="lg" className="tableResidentialInHamlet">
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
                <Table striped bordered hover size="lg" className="tableResidentialInHamlet">
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
                    <div className="flexFormInput">
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
                            }}> Xác nhận </Button>
                        </Form.Group>
                    </div>
                </div>
            </div>
        )
    }

    const GetDataToStatic = async () => {
        setHeightScreen(window.screen.height)
        setShowTableMultiStatic(true)
        const provinceList = provinceCodes.map((province) => (province.value))
        console.log(provinceList)
        const optionChooseList = optionCodes.map((option) => (option.value))
        setOptionList(optionChooseList)
        if (role === "A1") {
            const data = {
                division: "province",
                codes: provinceList,
                properties: optionChooseList
            }
            console.log(data)
            const response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataMultiStatic(response.data)
            console.log(response.data)
        } else if (role === 'A2') {
            const data = {
                "division": "district",
                "codes": provinceList,
                "properties": optionChooseList
            }
            const response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataMultiStatic(response.data)
            console.log(response.data)
        } else if (role === 'A3') {
            const data = {
                "division": "ward",
                "codes": provinceList,
                "properties": optionChooseList
            }
            const response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataMultiStatic(response.data)
            console.log(response.data)
        } else if (role === 'B1') {
            const data = {
                "division": "hamlet",
                "codes": provinceList,
                "properties": optionChooseList
            }
            const response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataMultiStatic(response.data)
            console.log(response.data)
        } else if (role === 'B2') {
            const data = {
                "division": "hamlet",
                "codes": [user],
                "properties": optionChooseList
            }
            const response = await axios.post("http://localhost:8080/api/v1/statistics/population/division", data, config)
            setDataMultiStatic(response.data)
            console.log(response.data)
        }
    }

    const SelectOption = () => {
        return (
            <div className='showSelectMultiOption'>
                <div className="titleSelectYear">THỐNG KÊ TÙY CHỌN</div>
                <div>
                    <Form.Group className="formInputMultiOption" style={{ marginBottom: '20px' }}>
                        <Form.Label style={{ width: '300px', marginTop: '6px' }}>1*. Chọn thuộc tính thống kê</Form.Label>
                        <Select className="selectMultiOption" placeholder="Chọn danh sách thuộc tính cần thống kê" options={listOption} isMulti onChange={(e) => {
                            setOptionCodes(e)
                            setShowTableMultiStatic(false)
                        }} />
                    </Form.Group>
                    {(role !== 'B2') ? <Form.Group className="formInputMultiOption">
                        <Form.Label style={{ width: '300px', marginTop: '6px' }}>2*. Chọn đơn vị hành chính</Form.Label>
                        <Select className="selectMultiOption" placeholder="Chọn danh sách đơn vị hành chính cần thống kê" options={optionStaticProvinces} isMulti onChange={(e) => {
                            setProvinceCodes(e)
                            setShowTableMultiStatic(false)
                        }} />
                    </Form.Group> : null}
                    <Form.Group style={{ marginLeft: '30px', marginTop: '20px' }}>
                        <Button onClick={() => {
                            GetDataToStatic()
                        }}> Xác nhận </Button>
                    </Form.Group>
                </div>
            </div>
        )
    }

    const PageStatic = () => {
        return (
            <div className="flexStatic">
                <TreeUnits className="flex_first" />
                <div className="flex_second">
                    {(showMultiStatic) ? SelectOption() : null}
                    {(showAge) ? SelectYear() : null}
                    <div className="chartStatic">
                        {(showChart || showChartAge) ? <div className='titleStatic'>BIỂU ĐỒ THỂ HIỆN CƠ CẤU {titlePage}</div> : null}
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

    const ModalWarningEndYear = () => {
        return (
          <Modal show={showWarningEndYear}>
            <Modal.Header className='headerModal'>
              <Modal.Title className='titleModal'>LỖI</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              Năm kết thúc không thể lớn hơn năm hiện tại
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => {
                setShowWarningEndYear(false)
              }}>
                Đóng
              </Button>
            </Modal.Footer>
          </Modal>
        )
      }

    return (
        <div>
            <NavbarPage />
            {PageStatic()}
            <ModalWarningEndYear />
        </div>
    );
}

export default BasicStatis;

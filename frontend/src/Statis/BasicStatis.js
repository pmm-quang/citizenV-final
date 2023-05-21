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

ChartJS.register(CategoryScale);
ChartJS.register(ArcElement, Tooltip, Legend);

function BasicStatis() {
    const [option, setOption] = useState();
    const [dataStatic, setDataStatic] = useState([]);
    const [dataChart, setDataChart] = useState({
        labels: [],
        datasets: [
            {
                label: 'Tổng dân số',
                data: [],
            },
        ],
    });

    const GetDataStatic = async (code) => {
        let response;
        if (code === 'region') {
            response = await axios.get("http://localhost:8080/api/v1/statistics/population/region")
        } else {
            response = await axios.get("http://localhost:8080/api/v1/statistics/population/citizen?property=" + code)
        }
        setOption(code)
        setDataStatic(response.data)
        setDataChart({
            labels: response.data.map(item => item.name),
            datasets: [
                {
                    label: 'Tổng dân số',
                    data: response.data.map(item => item.population),
                },
            ],
        })
        console.log(dataChart)
    }

    useEffect(() => {
    }, [])

    const SelectOption = () => {
        return (
            <div className="selectOption">
                <div className='title'>Chọn thuộc tính cần thống kê: </div>
                <Form.Select className='optionInput' value={option} onChange={(e) => GetDataStatic(e.target.value)}>
                    <option></option>
                    <option value={'sex'}>Giới tính</option>
                    <option value={'maritalStatus'}>Tình trạng hôn nhân</option>
                    <option value={'bloodType'}>Nhóm máu</option>
                    <option value={'ethnicity'}>Dân tộc</option>
                    <option value={'religion'}>Tôn giáo</option>
                    <option value={'maritalStatus'}>Tình trạng hôn nhân</option>
                    <option value={'region'}>Dân cư theo vùng</option>
                </Form.Select>
            </div>
        )
    }


    const listStatics = dataStatic.map((post, index) =>
        <tr key={index}>
            <td>{post.name}</td>
            <td>{post.population}</td>
        </tr>
    )

    const TableStatic = () => {
        return (
            <div>
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

    const SexStatic = () => {
        return (
            <div className="flexStatic">
                <div>
                    <div className="chartStatic">
                        {(option === undefined) ? null : <div className='titleStatic'>BIỂU ĐỒ THỂ HIỆN CƠ CẤU</div>}
                        {(option !== 'region') ? <Pie data={dataChart} className='chart' /> : null}
                        {(option === 'region') ? <Bar data={dataChart} className='chart' /> : null}
                    </div>
                </div>
                <div className='flex_second'>
                    {(option === undefined) ? null : <div className='titleStatic'>BẢNG THỐNG KÊ TỔNG DÂN SỐ</div>}
                    {(option === undefined) ? null : <TableStatic />}
                </div>
            </div>
        )
    }

    return (
        <div>
            <NavbarPage />
            <div>
                <SelectOption />
            </div>
            <SexStatic />
        </div>
    );
}

export default BasicStatis;

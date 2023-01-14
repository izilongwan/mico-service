package com.test.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.common.entity.R;
import com.test.service.PoiService;

@Service
public class PoiServiceImpl implements PoiService {

    @Override
    public ResponseEntity<Object> download(HttpServletResponse response) {
        try {
            HttpHeaders headers = buildDownloadHeaders("null.xlsx");
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            buildWorkBook(buildLists()).write(out);

            // ServletOutputStream outputStream = response.getOutputStream();
            // String s = String.format("attachment;filename=%s", "null.xlsx");
            // response.addHeader(HttpHeaders.CONTENT_DISPOSITION, s);
            // response.addHeader(HttpHeaders.CONTENT_TYPE,
            // MediaType.APPLICATION_OCTET_STREAM_VALUE);
            // outputStream.write(out.toByteArray());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(out.size())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(out.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.ok()
                    .body(R.ERROR(e, e.getMessage()));
        }
    }

    private HttpHeaders buildDownloadHeaders(String filename) {
        HttpHeaders headers = new HttpHeaders();
        String s = String.format("attachment;filename=%s", filename);

        headers.add(HttpHeaders.CONTENT_DISPOSITION, s);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        headers.addAll(headers);

        return headers;
    }

    public List<List<Object>> buildLists() {
        List<Object> list = Collections.emptyList();
        List<List<Object>> lists = new ArrayList<>();
        lists.add(list);

        return lists;
    }

    public <T> XSSFWorkbook buildWorkBook(List<List<T>> list) {
        ClassPathResource resource = new ClassPathResource("t1.xlsx");
        // URL resource2 = this.getClass().getResource("/xxx");

        try {
            // SXSSFWorkbook 百万数据
            XSSFWorkbook wb = new XSSFWorkbook(resource.getInputStream());
            wb.createSheet("v3");
            wb.createSheet("v4");

            return wb;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

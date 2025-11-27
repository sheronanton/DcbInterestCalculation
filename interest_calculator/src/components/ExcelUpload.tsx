import React, { useState, useEffect } from "react";

type ResultEntry = {
  month: number;
  year: number;
  demand: number;
  openingBalance: number;
  closingBalance: number;
  overdueAmount: number;
  interest: number;
};

type UploadResponse = {
  results: ResultEntry[];
  totalClosingBalance: number;
  totalInterest: number;
};

const monthNames = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

const ExcelUpload: React.FC = () => {
  let type = 2; // 1 = development, 2 = production

  const BASE_URL = type === 1 ? "http://localhost:8080/" : "/intCalc/";

  const [mode, setMode] = useState<"localbody" | "private">("localbody");
  const [file, setFile] = useState<File | null>(null);
  const [data, setData] = useState<ResultEntry[]>([]);
  const [totals, setTotals] = useState<{
    totalClosingBalance: number;
    totalInterest: number;
  }>({
    totalClosingBalance: 0,
    totalInterest: 0,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [responseData, setResponseData] = useState<UploadResponse | null>(null);

  // Extracted backend fetch logic
  const fetchData = async (f: File, currentMode: "localbody" | "private") => {
    setError("");
    setLoading(true);
    const formData = new FormData();
    formData.append("file", f);
    formData.append("mode", currentMode);

    try {
      const response = await fetch(`${BASE_URL}api/excel/upload`, {
        method: "POST",
        body: formData,
      });

      if (!response.ok) throw new Error("Failed to process file");

      const result: UploadResponse = await response.json();
      setData(result.results);
      setTotals({
        totalClosingBalance: result.totalClosingBalance,
        totalInterest: result.totalInterest,
      });
      setResponseData(result);
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "Unknown error uploading file"
      );
    } finally {
      setLoading(false);
    }
  };

  // File input handler
  const handleFile = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const uploadedFile = event.target.files?.[0];
    if (!uploadedFile) return;
    setFile(uploadedFile);
    await fetchData(uploadedFile, mode);
  };

  // When toggle mode changes, rerun fetch if file already present
  useEffect(() => {
    if (file) {
      fetchData(file, mode);
    }
    // eslint-disable-next-line
  }, [mode]);

  // Download as Excel
  const handleDownload = async () => {
    if (!responseData) return;

    try {
      const response = await fetch(`${BASE_URL}api/excel/download`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(responseData),
      });

      if (!response.ok) throw new Error("Failed to download file");

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "interest_calculation.xlsx";
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    } catch (err) {
      alert(err instanceof Error ? err.message : "Download failed");
    }
  };

  return (
    <>
      <h2>Urban Demand Interest Calculator</h2>
      <div className="calculator-file">
        <input type="file" accept=".xlsx,.xls" onChange={handleFile} />
      </div>
      <div className="calculator-toggle-switch">
        <span className={mode === "localbody" ? "active" : ""}>Local Body</span>
        <label className="switch">
          <input
            type="checkbox"
            checked={mode === "private"}
            onChange={() =>
              setMode(mode === "localbody" ? "private" : "localbody")
            }
          />
          <span className="slider"></span>
        </label>
        <span className={mode === "private" ? "active" : ""}>Private</span>
      </div>
      {loading && <p>Processing...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
      {Array.isArray(data) && data.length > 0 && (
        <>
          <div
            style={{
              textAlign: "center",
              marginBottom: "8px",
              fontWeight: "600",
            }}
          >
            Mode: {mode === "localbody" ? "Local Body" : "Private"}
          </div>
          <table className="calculator-table">
            <thead>
              <tr>
                <th>Month</th>
                <th>Year</th>
                <th>Opening Balance</th>
                <th>Demand</th>
                <th>Overdue Amount</th>
                <th>Closing Balance</th>
                <th>Interest</th>
              </tr>
            </thead>
            <tbody>
              {data.map((row, idx) => (
                <tr key={idx}>
                  <td>{monthNames[row.month - 1]}</td>
                  <td>{row.year}</td>
                  <td>{row.openingBalance}</td>
                  <td>{row.demand}</td>
                  <td>{row.overdueAmount}</td>
                  <td>{row.closingBalance}</td>
                  <td
                    title={
                      row.overdueAmount > 0
                        ? mode === "private"
                          ? `Interest = Overdue Amount (${row.overdueAmount}) x 2% = ${row.interest}`
                          : `Interest = Overdue Amount (${row.overdueAmount}) x 0.5% = ${row.interest}`
                        : "No interest (less than X months overdue)"
                    }
                    style={{ cursor: "help" }}
                  >
                    {row.interest}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div className="calculator-totals">
            Total Closing Balance: {totals.totalClosingBalance} <br />
            Total Interest Closing Balance: {totals.totalInterest}
          </div>
          <button
            className="calculator-btn"
            onClick={handleDownload}
            style={{
              marginTop: 16,
              padding: "10px 20px",
              fontSize: "16px",
              cursor: "pointer",
            }}
          >
            Download as Excel
          </button>
        </>
      )}
    </>
  );
};

export default ExcelUpload;

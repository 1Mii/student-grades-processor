import { useRef, useState } from "react";
import "./UploadFile.css";

function UploadFile({ onUploadSuccess }) {
  const [file, setFile] = useState(null);
  const [validEntries, setValidEntries] = useState(0);
  const [invalidEntries, setInvalidEntries] = useState(0);
  const fileInputRef = useRef();

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async (event) => {
    event.preventDefault();

    if (!file) {
      alert("Por favor, selecione um ficheiro primeiro.");
      return;
    }

    const form = new FormData();
    form.append("file", file);

    try {
      const response = await fetch(`${process.env.REACT_APP_API_URL}/api/upload-file`, {
        method: "POST",
        body: form
      });

      if (!response.ok) {
        throw new Error("Erro no servidor");
      }

      const data = await response.json();

      alert("Ficheiro enviado com sucesso!");
      setValidEntries(prev => prev + (data.valid_records || 0));
      setInvalidEntries(prev => prev + (data.invalid_records || 0));

      if (onUploadSuccess) onUploadSuccess();

      setFile(null);
      fileInputRef.current.value = "";
    } catch (error) {
      console.error("Error:", error);
      alert("Erro ao enviar ficheiro.");
    }
  };

  return (
    <div>
      <h2 className="upload-card__title">Upload CSV</h2>

      <form className="upload-form" onSubmit={handleUpload}>
        <input
          className="upload-form__input"
          type="file"
          accept=".csv"
          onChange={handleFileChange}
          ref={fileInputRef}
        />
        <button className="upload-form__button" type="submit" disabled={!file}>
          Send
        </button>
      </form>

      {file && (
        <p className="upload-file-name">
          Selected file: <strong>{file.name}</strong>
        </p>
      )}

      <div className="upload-stats">
        <p className="upload-stats__item">
          <strong>Valid:</strong> {validEntries}
        </p>
        <p className="upload-stats__item">
          <strong>Invalid:</strong> {invalidEntries}
        </p>
      </div>
    </div>
  );
}

export default UploadFile;
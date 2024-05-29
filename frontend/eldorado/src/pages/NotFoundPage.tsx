import {useNavigate} from "react-router-dom";
import {Button} from "@/components/ui/button";
import {useTranslation} from "react-i18next";
import eldoLogo from "@/assets/eldorado.png";

const NotFoundPage = () => {
    const {t} = useTranslation();
    const navigate = useNavigate();

    return (
        <div className="flex flex-col items-center justify-center h-screen">
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/3"/>
            <h1 className="text-3xl font-bold mb-4">{t("notFound.title")}</h1>
            <p className="text-lg mb-8">{t("notFound.description")}</p>
            <Button onClick={() => navigate("/home")}
                    className="transition-colors hover:text-foreground">
                {t("notFound.home")}
            </Button>
        </div>
    );
};

export default NotFoundPage;

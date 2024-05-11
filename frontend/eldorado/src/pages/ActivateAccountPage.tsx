import {useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {api} from '@/api/api.ts';
import {useToast} from '@/components/ui/use-toast.ts';
import {Button} from "@/components/ui/button.tsx";
import eldoLogo from "@/assets/eldorado.png";
import {Card, CardDescription, CardHeader, CardTitle,} from "@/components/ui/card"

function ActivateAccountPage() {
    const {token} = useParams<{ token: string }>();
    const {toast} = useToast();
    const navigate = useNavigate();

    useEffect(() => {
        api.activateAccount(token!)
            .then(() => {
                toast({
                    title: "Account activated",
                    description: "Your account has been successfully activated. You can now log in.",
                    action: (
                        <div>
                            <Button onClick={() => {
                                navigate('/login', {replace: true});

                            }}>
                                Log in
                            </Button>
                        </div>
                    ),
                });
            })
            .catch((error) => {
                toast({
                    title: "Error",
                    description: "There was an error activating your account. Please try again later.",
                });
                console.log(error.response.data);
            });
    }, [token, toast, navigate]);

    return (
        <div>
            <img src={eldoLogo} alt="Eldorado" className="mx-auto h-auto w-1/2"/>
            <Card>
                <CardHeader>
                    <CardTitle>Almost there...</CardTitle>
                    <CardDescription>Your account is being activated. Please wait.</CardDescription>
                </CardHeader>
            </Card>
        </div>
    );
}

export default ActivateAccountPage;